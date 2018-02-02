package test.storm;

import backtype.storm.Config;
import backtype.storm.utils.DRPCClient;
import org.apache.thrift7.TException;

/**
 */
public class DrpcTestClient {
  public static void main(String[] args) throws TException {
    Config conf = new Config();
    conf.put("storm.thrift.transport", "backtype.storm.security.auth.SimpleTransportPlugin");
    conf.put(Config.STORM_NIMBUS_RETRY_TIMES, 3);
    conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL, 10);
    conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL_CEILING, 20);
    conf.put(Config.DRPC_MAX_BUFFER_SIZE, 1048576);

    DRPCClient cl = new DRPCClient(conf,"localhost", 3772);
    if (args.length != 2){
      System.err.println(DrpcTestClient.class.getSimpleName() + " required args: <functionName> <arguments>");
    }
    else
    {
      String func = args[0];
      String argument = args[1];
      System.out.println(cl.execute(func, argument));
    }
  }
}