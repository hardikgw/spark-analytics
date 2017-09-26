#!/usr/bin/env bash

grep -Eo "<influences>" yagoFacts.tsv > yagoFactInfluence.tsv
cut -f 3 yagoFacts_del.tsv | sort | uniq > yagoFactsPredicates.txt

grep -E "<actedIn>|<directed>" yagoFacts.tsv > yagoFactMovies.tsv

#docker exec master hdfs dfs -mkdir /data
#cp /Users/hp/workbench/projects/gmu/spark-hin/src/main/resources/data/yago/yagoFacts.tsv data/master/data/.
#docker exec master hdfs dfs -put data/yagoFacts.tsv /data/yagoFacts.tsv
#docker exec master hdfs dfs -ls /data