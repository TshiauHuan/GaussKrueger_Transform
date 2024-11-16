//类的定义及成员变量
public class gs {
    private double PI=3.1415926;
    private double a, b, e1, e2, A1, B1, C1, D1, E1, F1, G1;//用于存储椭球的参数和中间计算值

    // 构造函数初始化椭球参数
    public gs(double a1, double b1) {
        this.a = a1;
        this.b = b1;
        this.e1 = Math.sqrt((a1 * a1 - b1 * b1) / (a1 * a1));//计算椭球的第一偏心率
        this.e2 = Math.sqrt((a1 * a1 - b1 * b1) / (b1 * b1));//计算椭球的第二偏心率
        //计算各参数 A1, B1, C1, D1, E1, F1, G1
        this.A1 = 1.0 +3.0/4.0*Math.pow(e1,2)+45.0/64.0*Math.pow(e1,4)+175.0/256.0*Math.pow(e1,6)+
                11025.0/16384.0*Math.pow(e1,8)+43659.0/65536.0*Math.pow(e1,10)+693693.0/1048576.0*Math.pow(e1,12);

        this.B1 = 3.0/8.0*Math.pow(e1,2)+15.0/32.0*Math.pow(e1,4)+525.0/1024.0*Math.pow(e1,6)+2205.0/4096.0*Math.pow(e1,8)
                +72765.0/131072.0*Math.pow(e1,10)+297297.0/524288.0*Math.pow(e1,12);

        this.C1 = 15.0/256.0*Math.pow(e1,4)+105.0/1024.0*Math.pow(e1,6)+2205.0/16384.0*Math.pow(e1,8)
                +10396.0/65536.0*Math.pow(e1,10)+1486485.0/8388608.0*Math.pow(e1,12);

        this.D1 = 35.0/3072.0*Math.pow(e1,6)+105.0/4096.0*Math.pow(e1,8)+10395.0/262144.0*Math.pow(e1,10)
                +55055.0/1048576.0*Math.pow(e1,12);

        this.E1 = 315.0/131072.0*Math.pow(e1,8)+3465.0/524288.0*Math.pow(e1,10)+99099.0/8388608.0*Math.pow(e1,12);

        this.F1 = 693.0/1310720.0*Math.pow(e1,10)+9909.0/5242880.0*Math.pow(e1,12);

        this.G1 = 1001.0/8388608.0*Math.pow(e1,12);
    }

    // 计算卯酉圈曲率半径，输入参数 B 是纬度（以弧度为单位）
    private double CalculateN(double B) {
        return a/Math.sqrt(1.0 - e1 * e1 * Math.sin(B) * Math.sin(B));
    }

    // 计算中央子午线弧长，输入参数 B 是纬度（以弧度为单位）
    private double CalculateX(double B) {
        return a*(1.0-e1*e1)*(A1*B-B1*Math.sin(2*B)+C1*Math.sin(4*B)-D1*Math.sin(6*B)+E1*Math.sin(8*B)-
                F1*Math.sin(10*B)+G1*Math.sin(12*B));
    }

    // 3度分带转换x值求解
    public double gsklv3gtransformX(double Lon, double Lat) {
        double L0 = 3.0 * (Math.floor((Lon-1.5) / 3.0) + 1.0); //3度分带中央经线
        double l = (Lon - L0) * PI / 180.0; //当地经度减去中央子午线经度 （转换为弧度）
        double B = Lat * PI / 180.0; //纬度（转换为弧度）
        double N = CalculateN(B); //计算曲率半径
        double X = CalculateX(B); //计算弧长
        double t = Math.tan(B);
        double η2 = this.e2 * this.e2 * Math.cos(B) * Math.cos(B);

        return X +N*t*Math.cos(B)*Math.cos(B)*l*l*(0.5+(1.0/24.0)*(5.0-t*t+9.0*η2+4.0*Math.pow(η2,2))*
                Math.pow(l*Math.cos(B),2)+1.0/720.0*(61.0-58.0*t*t+Math.pow(t,4))*Math.pow(l*Math.cos(B),4));
    }

    // 3度分带转换y值求解
    public double gsklv3gtransformY(double Lon, double Lat) {
        double L0 = 3.0 *( Math.floor((Lon-1.5) / 3.0) + 1.0); //3度分带中央经线
        double l = (Lon - L0) * PI / 180.0; //当地经度减去中央子午线经度 （转换为弧度）
        double B = Lat * PI / 180.0; //纬度（转换为弧度）
        double N = CalculateN(B); //计算曲率半径
        double t = Math.tan(B);
        double η2 = this.e2 * this.e2 * Math.cos(B) * Math.cos(B);
        return 500000.0 + N * Math.cos(B)*l*(1.0+(1.0/6.0)*(1.0-t*t+η2)*Math.cos(B)*Math.cos(B)*l*l+(1.0/120.0)*
                (5.0-18.0*(t*t)+Math.pow(t,4)+14.0*(η2)-58.0*(t*t*η2))*Math.pow(Math.cos(B),4)*Math.pow(l,4));

    }

    // 6度分带转换x值求解
    public double gsklv6gtransformX(double Lon, double Lat) {
        double L0 = 6.0 * (Math.floor(Lon / 6.0) + 1.0)-3.0; //6度分带中央经线
        double l = (Lon - L0) * PI / 180.0; //当地经度减去中央子午线经度 （转换为弧度）
        double B = Lat * PI / 180.0; //纬度（转换为弧度）
        double N = CalculateN(B); //计算曲率半径
        double X = CalculateX(B); //计算弧长
        double t = Math.tan(B);
        double η2 = this.e2*this.e2 * Math.cos(B) * Math.cos(B);
        return X + N*t*Math.cos(B)*Math.cos(B)*l*l*(0.5+(1.0/24.0)*(5.0-t*t+9.0*η2+4.0*Math.pow(η2,2))*
                Math.pow(l*Math.cos(B),2)+1.0/720.0*(61.0-58.0*t*t+Math.pow(t,4))*Math.pow(l*Math.cos(B),4));
    }

    // 6度分带转换y值求解
    public double gsklv6gtransformY(double Lon, double Lat) {
        double L0 = 6.0 * (Math.floor(Lon / 6.0) + 1.0)-3.0; //6度分带中央经线
        double l = (Lon - L0) * PI / 180.0; //当地经度减去中央子午线经度 （转换为弧度）
        double B = Lat * PI / 180.0; //纬度（转换为弧度）
        double N = CalculateN(B); //计算曲率半径
        double η2 = this.e2* this.e2 * Math.cos(B) * Math.cos(B);
        double t = Math.tan(B);
        return 500000.0 + N * Math.cos(B)*l*(1.0+(1.0/6.0)*(1.0-t*t+η2)*Math.cos(B)*Math.cos(B)*l*l+(1.0/120.0)*
                (5.0-18.0*(t*t)+Math.pow(t,4)+14.0*(η2)-58.0*(t*t*η2))*Math.pow(Math.cos(B),4)*Math.pow(l,4));

    }
}