#!/usr/bin/env bash

grep -Eo "<influences>" yagoFacts.tsv > yagoFactInfluence.tsv
cut -f 3 yagoFacts_del.tsv | sort | uniq > yagoFactsPredicates.txt

grep -E "<actedIn>|<directed>" yagoFacts.tsv > yagoFactMovies.tsv

#docker exec master hdfs dfs -mkdir /data
#cp /Users/hp/workbench/projects/gmu/spark-hin/src/main/resources/data/yago/yagoFacts.tsv data/master/data/.
#docker exec master hdfs dfs -put data/yagoFacts.tsv /data/yagoFacts.tsv
#docker exec master hdfs dfs -ls /data

docker exec master hdfs dfs -put /data/tweeter/2017100309.txt /data/tweeter/2017100309.txt
docker exec master hdfs dfs -put /data/tweeter/2017100314.txt /data/tweeter/2017100314.txt
docker exec master hdfs dfs -put /data/tweeter/2017100315.txt /data/tweeter/2017100315.txt
