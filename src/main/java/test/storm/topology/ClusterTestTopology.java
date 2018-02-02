package test.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.tuple.Fields;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.MapGet;
import storm.trident.testing.MemoryMapState;
import test.storm.topology.spout.FakeTweetsBatchSpout;

/**
 * Silly test topology to test the cluster
 *
 */
public class ClusterTestTopology {
  public static void main(String[] args) throws Exception {
    Config conf = new Config();

    // Submits the topology
    String topologyName = args[0];
    boolean remote = args[1].equalsIgnoreCase("remote");

    conf.setNumWorkers(8);

    FakeTweetsBatchSpout fakeTweets = new FakeTweetsBatchSpout(10);

    TridentTopology topology = new TridentTopology();
    TridentState countState = topology.newStream("spout", fakeTweets)
      .groupBy(new Fields("actor"))
      .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"));



    if (remote) {
      topology.newDRPCStream("count_per_actor")
        .stateQuery(countState, new Fields("args"), new MapGet(), new Fields("count"));

      StormSubmitter.submitTopology(topologyName, conf, topology.build());
    }
    else
    {
      LocalDRPC client = new LocalDRPC();
  		LocalCluster cluster = new LocalCluster();
      topology.newDRPCStream("count_per_actor", client)
        .stateQuery(countState, new Fields("args"), new MapGet(), new Fields("count"));

	  	cluster.submitTopology(topologyName, conf, topology.build());
    }
  }
}
