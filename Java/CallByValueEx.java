public class CallByValueEx {
    static void update(int value){
        System.out.println("원시 타입 값 변경 함수 실행");
        value = Integer.MAX_VALUE;
    }
    static void update(A obj){
        System.out.println("참조 타입 값 변경 함수 실행");
        obj.setValue1(Integer.MAX_VALUE);
        obj.setValue2(Integer.MAX_VALUE);
    }
    static void change(A obj){
        System.out.println("참조 타입의 객체 변경 함수 실행");
        obj = new A(100, 100);
    }

    public static void main(String [] args){
        int a = 100;

        A obj1 = new A(10, 10);
        A obj2 = new A(20, 20);

        System.out.println("----- 변경 전 -----");
        System.out.println("a : " + a);
        System.out.println("obj1.getValue1() : " + obj1.getValue1());
        System.out.println("obj1.getValue2() : " + obj1.getValue2());
        System.out.println("obj2.getValue1() : " + obj2.getValue1());
        System.out.println("obj2.getValue2() : " + obj2.getValue2());
        System.out.println("obj1의 해시코드 : " + System.identityHashCode(obj1));
        System.out.println("obj2의 해시코드 : " + System.identityHashCode(obj2));
        System.out.println("----- 함수 실행 -----");
        update(a);
        update(obj1);
        change(obj1);
        System.out.println("----- 변경 후 -----");
        System.out.println("a : " + a);
        System.out.println("obj1.getValue1() : " + obj1.getValue1());
        System.out.println("obj1.getValue2() : " + obj1.getValue2());
        System.out.println("obj2.getValue1() : " + obj2.getValue1());
        System.out.println("obj2.getValue2() : " + obj2.getValue2());
        System.out.println("obj1의 해시코드 : " + System.identityHashCode(obj1));
        System.out.println("obj2의 해시코드 : " + System.identityHashCode(obj2));
    }
}

class A {
    private int value1;
    private int value2;

    A(int value1, int value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    public void setValue1(int value1){
        this.value1 = value1;
    }

    public void setValue2(int value2){
        this.value2 = value2;
    }

    public int getValue1(){
        return value1;
    }

    public int getValue2(){
        return value2;
    }
}