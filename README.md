# Metabolite foundInTaxon Species Nanopublications

Nanopublications sourced from Wikidata, taking into account provenance given,
ideally with DOI.

## Generating

First, the data is downloaded from Wikidata:

```shell
curl -H "Accept: text/tab-separated-values" \
     --data-urlencode query@wikidata.rq \
     -G https://query.wikidata.org/bigdata/namespace/wdq/sparql \
     -o data.tsv
```

Then a Groovy script converts it into nanopublications:

```shell
groovy metaboliteSpecies.groovy > metaboliteSpecies.trig
```

## License

The data is CCZero. The code is GPL v3.
