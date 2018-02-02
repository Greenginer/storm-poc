# storm-starter

This is a repo I'm using to learn storm, trident and using it all with storm-docker

# pre-reqs
Start with installing [docker-compose](https://docs.docker.com/compose/install/)

Refer to use usage section of the [storm-docker](https://github.com/undeadops/storm-docker/blob/master/README.md#usage) project to load storm cluster in docker

Install storm cli, follow steps to [setup development environment](http://storm.apache.org/documentation/Setting-up-development-environment.html) on apache storm documentation site

# building
This project is maven enabled, run ```mvn package``` then check the target/ directory for the jar to submit to storm

# running in storm
I'll use a standard where the 2nd param of the topology arguments will be used to specify 'local' or 'remote'. Local will use 'LocalCluster' class to allow rapid development/testing of the topology, while remote will be used for execution on a live cluster

```
$ storm jar target/test-etl-cli-0.1.0.jar test.storm.topology.ClusterTestTopology cluster-test remote
```

The ```ClusterTestTopology``` starts a topology that has two parts. First one uses the ```FakeTweetsBatchSpout``` to generate fake tweets and puts them in a memory map. There is also a drpc spout that accepts one argument 'actor name'.

There is a test DrpcClient that can be used to pull items from the dprc spout.

```
$ java -cp test-etl-cli-0.1.0-jar-with-dependencies.jar:/opt/apache-storm-0.10.0/lib/storm-core-0.10.0.jar test.storm.DrpcTestClient count_per_actor mary
```
