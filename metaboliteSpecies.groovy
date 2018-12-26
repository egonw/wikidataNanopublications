@Grab(group='org.nanopub', module='nanopub', version='1.18')
@Grab(group='org.openrdf.sesame', module='sesame-sail-memory', version='4.1.2')
@Grab(group='org.openrdf.sesame', module='sesame-repository-sail', version='4.1.2')
@Grab(group='org.openrdf.sesame', module='sesame-model', version='4.1.2')


import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.nanopub.NanopubUtils;
import org.nanopub.trusty.MakeTrustyNanopub;

import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import org.openrdf.model.vocabulary.RDF;

MemoryStore store = new MemoryStore();
Repository repo = new SailRepository(store);
repo.initialize();

RepositoryConnection con = repo.getConnection();

factory = ValueFactoryImpl.getInstance();

con.add(
  factory.createURI("http://www.bigcat.unimaas.nl/nanopubs/wikidata/tmp/np1"),
  RDF.TYPE, Nanopub.NANOPUB_TYPE_URI,
  factory.createURI("http://www.bigcat.unimaas.nl/nanopubs/wikidata/tmp/np1#head")
)

prefixes = new ArrayList<String>();
prefixes.add("np");
namespaces = new HashMap<String, String>();
namespaces.put("np", "http://www.nanopub.org/nschema#");

StringBuffer outputBuffer = new StringBuffer();

results = con.getStatements(null, RDF.TYPE, Nanopub.NANOPUB_TYPE_URI);
while (results.hasNext()) {
  nextResult = results.next()
  println nextResult.class.name
  Resource nanopubId = nextResult.getSubject();
  println nanopubId.class.name
  if (nanopubId instanceof IRI) {
    Nanopub nanopub = new NanopubImpl(repo, nanopubId, prefixes, namespaces);
    nanopub = MakeTrustyNanopub.transform(nanopub);
    outputBuffer.append(NanopubUtils.writeToString(nanopub, RDFFormat.TRIG)).append("\n\n");
  }
}

println outputBuffer.toString()
