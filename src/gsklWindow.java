import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class gsklWindow extends JPanel {
    //声明一组私有成员变量，用于创建不同的图形组件，如单选按钮、文本字段和按钮
    private JRadioButton radioWGS84, radioBeijing54, radioXian80;
    private JTextField textLon, textLat, textX, textY;
    private JButton buttonTransform;
    private JRadioButton radio3Degree, radio6Degree;

    public gsklWindow() { //构造函数

        setLayout(new GridLayout(8, 2, 5, 5)); //设置面板的布局为 8 行 2 列的网格布局，每个单元格之间有 5 像素的间距

        //创建三个单选按钮，用于选择坐标类型
        radioWGS84 = new JRadioButton("WGS84");
        radioBeijing54 = new JRadioButton("北京54");
        radioXian80 = new JRadioButton("西安80");
        //创建一个按钮组 bgCoordinate，并将这三个单选按钮添加到按钮组中，以确保只能选择其中一个
        ButtonGroup bgCoordinate = new ButtonGroup();
        bgCoordinate.add(radioWGS84);
        bgCoordinate.add(radioBeijing54);
        bgCoordinate.add(radioXian80);

        //添加标签和面板来显示和选择坐标类型
        add(new JLabel("选择坐标类型："));
        //将单选按钮添加到 coordinatePanel 中，并将该面板添加到主面板中
        JPanel coordinatePanel = new JPanel(new GridLayout(1, 3));
        coordinatePanel.add(radioWGS84);
        coordinatePanel.add(radioBeijing54);
        coordinatePanel.add(radioXian80);
        add(coordinatePanel);

        //添加标签和文本字段，用于输入经度和纬度
        add(new JLabel("输入经度："));
        textLon = new JTextField();
        add(textLon);

        add(new JLabel("输入纬度："));
        textLat = new JTextField();
        add(textLat);

        //创建两个单选按钮，用于选择分带类型
        radio3Degree = new JRadioButton("3度分带");
        radio6Degree = new JRadioButton("6度分带");
        //创建一个按钮组 bgDegree，并将这两个单选按钮添加到按钮组中
        ButtonGroup bgDegree = new ButtonGroup();
        bgDegree.add(radio3Degree);
        bgDegree.add(radio6Degree);

        //添加标签和面板来显示和选择分带类型
        add(new JLabel("选择分带类型："));
        JPanel degreePanel = new JPanel(new GridLayout(1, 2));
        //将单选按钮添加到 degreePanel 中，并将该面板添加到主面板中
        degreePanel.add(radio3Degree);
        degreePanel.add(radio6Degree);
        add(degreePanel);

        buttonTransform = new JButton("转换");
        //创建一个按钮 buttonTransform，用于执行坐标转换
        add(buttonTransform);
        //添加一个空标签作为占位符，以确保排版合理
        add(new JLabel());

        //添加标签和文本字段，用于显示转换后的 X 坐标和 Y 坐标
        add(new JLabel("X坐标："));
        textX = new JTextField();
        //将文本字段设置为不可编辑
        textX.setEditable(false);
        add(textX);

        add(new JLabel("Y坐标："));
        textY = new JTextField();
        textY.setEditable(false);
        add(textY);

        //为转换按钮添加事件监听器，当按钮被点击时，将调用 Controller 类的 actionPerformed 方法
        buttonTransform.addActionListener(new Controller());

    }

    private class Controller implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                double lon = Double.parseDouble(textLon.getText()); //从文本字段中读取并解析经度和纬度
                double lat = Double.parseDouble(textLat.getText());
                gs converter;

                //根据选择的坐标类型，创建相应的 gs 类实例（转换器)
                if (radioWGS84.isSelected()) {
                    converter = new gs(6378137.0, 6356752.3142);
                } else if (radioBeijing54.isSelected()) {
                    converter = new gs(6378245.0, 6356863.0188);
                } else if (radioXian80.isSelected()) {
                    converter = new gs(6378140.0, 6356755.2882);
                } else {
                    JOptionPane.showMessageDialog(null, "请选择一种坐标类型");//如果没有选择任何坐标类型，显示错误消息并返回
                    return;
                }

                //根据选择的分带类型，使用转换器将经纬度转换为 X 坐标和 Y 坐标
                double x, y;
                if (radio3Degree.isSelected()) {
                    x = converter.gsklv3gtransformX(lon, lat);
                    y = converter.gsklv3gtransformY(lon, lat);
                } else if (radio6Degree.isSelected()) {
                    x = converter.gsklv6gtransformX(lon, lat);
                    y = converter.gsklv6gtransformY(lon, lat);
                } else {
                    JOptionPane.showMessageDialog(null, "请选择一种分带类型");//如果没有选择任何分带类型，显示错误消息并返回
                    return;
                }

                //将转换结果格式化为字符串并显示在相应的文本字段中，保留11位小数
                textX.setText(String.format("%.11f", x));
                textY.setText(String.format("%.11f", y));

            } catch (NumberFormatException ex) { //捕获 NumberFormatException 异常，如果输入的经纬度值无效，显示错误消息
                JOptionPane.showMessageDialog(null, "请输入有效的经纬度值");
            }
        }
    }
}
