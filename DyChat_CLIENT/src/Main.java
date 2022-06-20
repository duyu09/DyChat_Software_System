import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main
{
    public static String CURRENT_TITLE=" DuyuChat - 迷你聊天软件v2.0";
    public static String SERVER="111.67.194.108";
    public static String PORT="9997";
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static JFrame form1=new JFrame(CURRENT_TITLE);
    public static Font font01=new Font("微软雅黑",0,12);
    public static Socket s=null;
    public static JTextArea text01=new JTextArea();
    public static JTextField textnick=new JTextField("default name ");
    public static JTextArea textmain=new JTextArea(2,33);
    public static long thid=0;
    public static boolean flag01=false;
    public static void main(String[] args)
    {
        form1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form1.setVisible(true);
        form1.setResizable(false);
        form1.setLocationRelativeTo(null);
        form1.setSize(450, 500);
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        form1.setLocation((width - form1.getWidth()) / 2, (height - form1.getHeight()) / 2);
        Container con = form1.getContentPane();

        JPanel pan01=new JPanel(new FlowLayout());pan01.setBackground(Color.BLACK);
        JPanel pan02=new JPanel(new FlowLayout(FlowLayout.LEFT));pan02.setBackground(Color.BLACK);
        con.setBackground(Color.BLACK);
        con.setLayout(new BorderLayout());
        text01.setBackground(new Color(50,50,50));
        text01.setForeground(Color.WHITE);text01.setFont(font01);text01.setEditable(false);
        JScrollPane jsp01=new JScrollPane(text01);
        con.add(jsp01,BorderLayout.CENTER);
        con.add(pan01,BorderLayout.NORTH);
        con.add(pan02,BorderLayout.SOUTH);
        JLabel lab01=new JLabel(CURRENT_TITLE);lab01.setForeground(Color.WHITE);
        lab01.setFont(font01);
        JPanel pantemp=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btncon=new JButton("连接");
        JButton btnsend=new JButton("发送");
        JButton btnset=new JButton("设置");
        JLabel nickname=new JLabel("昵称:");
        btncon.setFont(font01);btnset.setFont(font01);btnsend.setFont(font01);
        nickname.setForeground(Color.WHITE);nickname.setFont(font01);
        textnick.setFont(font01);
        textnick.setBackground(new Color(70,70,70));
        textnick.setForeground(Color.WHITE);
        pantemp.add(btncon);pantemp.add(btnset);pantemp.add(nickname);pantemp.add(textnick);
        pantemp.setBackground(Color.BLACK);
        pan01.add(lab01);pan01.add(pantemp);
        textmain.setFont(font01);
        textmain.setBackground(new Color(70,70,70));
        textmain.setForeground(Color.WHITE);
        pan02.add(textmain);
        pan02.add(btnsend);

//SetupForm f=new SetupForm();
        form1.validate();
        ActionListener listener02=new ActionListener() //连接按钮
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    try
                    {
                        if(s!=null)
                        {
                            s.close();//先把上一个断开
                        }
                    }catch (Exception e01){}
                    Main.s = new Socket(SERVER,Integer.parseInt(PORT));
                    InputMess in01=new InputMess(s);
                    thid=in01.getId();
                    Main.flag01=true;
                    in01.start();
                }
                catch (Exception e01)
                {
                    e01.printStackTrace();
                }
            }
        };btncon.addActionListener(listener02);
        ActionListener listener03=new ActionListener() //设置按钮
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SetupForm f=new SetupForm();
            }
        };btnset.addActionListener(listener03);
        ActionListener listener04=new ActionListener() //发送按钮
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Message mes02=new Message(Main.textnick.getText(),Main.textmain.getText());
                    ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                    os.writeObject(mes02);os.flush();
                    Main.textmain.setText("");
                }
                catch (Exception e01)
                {
                    System.err.println(e01.getMessage());
                    if(s==null && Main.flag01==false) JOptionPane.showMessageDialog(null, "请您首先连接！", "DuyuChat - 提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        };btnsend.addActionListener(listener04);

    }
}
class SetupForm extends JFrame
{
    public SetupForm()
    {
        setTitle("DuyuChat - 网络设置");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(275, 165);
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation((width - this.getWidth()) / 2, (height - this.getHeight()) / 2);
        Container con02 = getContentPane();
        con02.setLayout(new GridLayout(3,1));
        JPanel pan101=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pan102=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pan103=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btn101=new JButton("确定");
        JLabel lab101=new JLabel("服务器地址:");
        JLabel lab102=new JLabel("端口号:");
        JTextField text101=new JTextField(Main.SERVER,16);
        JTextField text102=new JTextField(Main.PORT,5);
        btn101.setFont(Main.font01);
        lab101.setFont(Main.font01);
        lab102.setFont(Main.font01);
        text101.setFont(Main.font01);
        text102.setFont(Main.font01);
        pan101.add(lab101);pan101.add(text101);
        pan102.add(lab102);pan102.add(text102);
        pan103.add(btn101);
        con02.add(pan101);
        con02.add(pan102);
        con02.add(pan103);
        validate();
        ActionListener listener101=new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.SERVER=text101.getText();
                Main.PORT=text102.getText();
                dispose();
            }
        };
        btn101.addActionListener(listener101);
    }
}
class Message implements Serializable
{
    String name;
    String information;
    LocalDateTime dt;
    public Message(String name,String information)
    {
        this.name=name;
        this.information=information;
    }
}
class InputMess extends Thread
{
    Socket s;
    public InputMess(Socket s)
    {
        this.s=s;
    }
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                Message mes=(Message) is.readObject();
                String dtstr=Main.dtf.format(mes.dt);
                String str=mes.name+":("+dtstr+")\n"+mes.information;
                //System.out.println(str);
                Main.text01.append(str+"\n");
                Main.text01.setCaretPosition(Main.text01.getDocument().getLength());
                Main.form1.requestFocus();
                if(Main.form1.getExtendedState()==JFrame.ICONIFIED)
                {
                    Toolkit.getDefaultToolkit().beep();
                    Main.form1.getFocusableWindowState();
                }
                if(s.isClosed()) stop();
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            if(Main.flag01==false) JOptionPane.showMessageDialog(null, "请您首先连接！", "DuyuChat - 提示", JOptionPane.ERROR_MESSAGE);
        }
    }
}