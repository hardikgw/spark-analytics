#!/usr/bin/env bash

grep -Eo "<influences>" yagoFacts.tsv > yagoFactInfluence.tsv
cut -f 3 yagoFacts_del.tsv | sort | uniq > yagoFactsPredicates.txt

grep -E "<actedIn>|<directed>" yagoFacts.tsv > yagoFactMovies.tsv