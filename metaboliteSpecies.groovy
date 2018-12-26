@Grab(group='org.nanopub', module='nanopub', version='1.18')

import org.nanopub.Nanopub;
import org.nanopub.NanopubCreator;
import org.nanopub.NanopubUtils;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.RDFFormat;

factory = ValueFactoryImpl.getInstance()

nanopubIRI = "http://www.bigcat.unimaas.nl/nanopubs/wikidata/tmp/np1"

creator = new NanopubCreator(nanopubIRI)
creator.addAssertionStatement(
  factory.createURI("http://subj"),
  factory.createURI("http://pred"),
  factory.createURI("http://obj")
)
creator.addAuthor(creator.getOrcidUri("0000-0001-7542-0286"))
creator.addProvenanceStatement(
  creator.getAssertionUri(),
  factory.createURI("http://pred/p"),
  factory.createURI("http://obj/p")
)


trustedPub = creator.finalizeTrustyNanopub()

outputBuffer = new StringBuffer();
outputBuffer.append(NanopubUtils.writeToString(trustedPub, RDFFormat.TRIG)).append("\n\n");

println outputBuffer.toString()
