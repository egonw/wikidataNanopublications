@Grab(group='org.nanopub', module='nanopub', version='1.18')

import org.nanopub.Nanopub;
import org.nanopub.NanopubCreator;
import org.nanopub.NanopubUtils;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.model.vocabulary.SKOS;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.rio.RDFFormat;

factory = ValueFactoryImpl.getInstance()

counter = 0

foundInTaxon = factory.createURI("http://www.wikidata.org/prop/direct/P703")
hasInChI = factory.createURI("http://semanticscience.org/resource/CHEMINF_000399")
hasSource = factory.createURI("http://semanticscience.org/resource/SIO_000253")

new File("data.tsv").eachLine { line ->
  fields = line.split("\t")
  if (fields[0] == "?metabolite") return
  counter++
  nanopubIRI = "http://purl.org/nanopub/temp/np" + counter

  creator = new NanopubCreator(nanopubIRI)
  creator.addTimestamp(new Date())
  creator.addNamespace("wd", "http://www.wikidata.org/entity/")
  creator.addNamespace("np", "http://www.nanopub.org/nschema#")
  creator.addNamespace("has-source", "http://semanticscience.org/resource/SIO_000253")
  creator.addNamespace("has-inchikey", "http://semanticscience.org/resource/CHEMINF_000399")
  creator.addNamespace("orcid", "http://orcid.org/")
  creator.addNamespace("wdt", "http://www.wikidata.org/prop/direct/")
  creator.addNamespace("owl", "http://www.w3.org/2002/07/owl#")
  creator.addNamespace("pav", "http://purl.org/pav/")
  creator.addNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#")
  creator.addNamespace("skos", "http://www.w3.org/2004/02/skos/core#")
  creator.addNamespace("dct", "http://purl.org/dc/terms/")
  creator.addNamespace("xsd", "http://www.w3.org/2001/XMLSchema#")

  metabolite = factory.createURI(fields[0])
  metaboliteName = factory.createLiteral(fields[1].replace("\"","").replace("@en",""), "en")
  taxon = factory.createURI(fields[3])
  taxonName = factory.createLiteral(fields[4].replace("\"","").replace("@en",""), "en")
  creator.addAssertionStatement(metabolite, foundInTaxon, taxon)
  creator.addAssertionStatement(metabolite, RDFS.LABEL, metaboliteName)
  creator.addAssertionStatement(taxon,      RDFS.LABEL, taxonName)
  inchikeyStr = fields[2]
  if (inchikeyStr != null && !inchikeyStr.isEmpty()) {
    inchikey = factory.createLiteral(inchikeyStr)
    creator.addAssertionStatement(metabolite, hasInChI, inchikey)
  }
  creator.addCreator(creator.getOrcidUri("0000-0001-7542-0286"))
  creator.addProvenanceStatement(
    creator.getAssertionUri(), hasSource, factory.createURI("http://www.wikidata.org/entity/Q2013")
  )
  source = factory.createURI(fields[5])
  creator.addProvenanceStatement(creator.getAssertionUri(), hasSource, source)
  sourceName = factory.createLiteral(fields[6].replace("\"","").replace("@en",""), "en")
  creator.addProvenanceStatement(source, RDFS.LABEL, sourceName)
  if (fields.length > 7) {
    doiStr = fields[7]
    if (doiStr != null && !doiStr.isEmpty()) {
      creator.addProvenanceStatement(
        source, OWL.SAMEAS,
        factory.createURI("https://doi.org/" + doiStr)
      )
    }
  }
  if (fields.length > 8) {
    taxonMatch = fields[8]
    if (taxonMatch != null && !taxonMatch.isEmpty()) {
      creator.addAssertionStatement(
        taxon, SKOS.EXACT_MATCH,
        factory.createURI(taxonMatch)
      )
    }
  }

  trustedPub = creator.finalizeTrustyNanopub()

  outputBuffer = new StringBuffer();
  outputBuffer.append(NanopubUtils.writeToString(trustedPub, RDFFormat.TRIG)).append("\n\n");
  println outputBuffer.toString()
}
