import javax.swing.JFrame;

public class gsklMain {
    public static void main(String args[]) {
        JFrame frame = new JFrame("高斯克吕格转换");
        frame.setLocation(600,300); //设置窗口在屏幕上的位置
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口的默认关闭操作，当用户点击关闭按钮时，程序将退出
        frame.getContentPane().add(new gsklWindow()); //获取窗口的内容面板，并添加一个 gsklWindow 实例
        frame.pack(); //调整窗口大小，以适应其内容的大小和布局
        frame.setVisible(true); //设置窗口为可见
    }
}

