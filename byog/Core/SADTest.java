package byog.Core;

public class SADTest {
    public static void main(String[] args) {
        StringArrayDeque sad = new StringArrayDeque();
        sad.enqueue("msg1");
        sad.enqueue("msg2");
        for (String s : sad) {
            System.out.println(s);
        }
        sad.enqueue("msg3");
        sad.enqueue("msg4");
        for (String s : sad) {
            System.out.println(s);
        }
        sad.enqueue("msg5");
        sad.enqueue("msg6");
        for (String s : sad) {
            System.out.println(s);
        }
        System.out.println("You hear faint shrieks in the distance.".length());
    }
}
