import java.net.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

public class Main
{
    public static String PORT="9997";
    public static HashMap<Integer,Socket> sarr=new HashMap<>();
    public static void main(String[] args)
    {
        try
        {
            ServerSocket ss=new ServerSocket(Integer.parseInt(PORT));
            int sum=0;
            while(true)
            {
                ++sum;
                Socket s=ss.accept();
                ProcessCli proc =new ProcessCli(s);
                proc.start();
                System.out.println("No."+sum+"process has started."+sarr.size()+" left.");
            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
class Message implements Serializable
{
    String name;
    String information;
    LocalDateTime dt=LocalDateTime.now();
    public Message(String name,String information)
    {
        this.name=name;
        this.information=information;
    }
}
class ProcessCli extends Thread
{
    Socket s;
    public ProcessCli(Socket s)
    {
        this.s = s;
        Main.sarr.put(Main.sarr.size()+1,s);
    }
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
                Message mes=(Message) ois.readObject();
                mes.dt=LocalDateTime.now();
                for(Integer num:Main.sarr.keySet())
                {
                    Socket soc=Main.sarr.get(num);
                    if(!soc.isConnected())
                    {
                        //Main.sarr.remove(num);
                        //stop();
                        continue;
                    }
                    ObjectOutputStream oos=new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(mes);oos.flush();
                }
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                Collection<Socket>col=Main.sarr.values();
                col.remove(s);
                try{Thread.sleep(1234);}
                catch(InterruptedException e9) {e9.printStackTrace();}
                stop();
            }
        }
    }
}