import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testTranslateWord {
    static Dictionary dictionary;
    static {
        dictionary = new Dictionary();
    }
    @Test
    public void test1(){ // traducere intre 2 limbi cu dictionar
        Assertions.assertEquals("pisică",
                dictionary.translateWord("chat", "fr", "ro"));
    }
    @Test
    public void test2(){ // traducere unui substantiv la plural
        Assertions.assertEquals("chats",
                dictionary.translateWord("pisici", "ro", "fr"));
    }
    @Test
    public void test3(){ // traducere in aceeasi limba
        Assertions.assertEquals("mergem",
                dictionary.translateWord("mergem", "ro", "ro"));
    }
    @Test
    public void test4(){ // traducere cand cuvantul nu exista in limba initiala
        Assertions.assertEquals("cal",
                dictionary.translateWord("cal", "ro", "fr"));
    }
    @Test
    public void test5(){ // traducere cand cuvantul nu exista in limba finala
        Assertions.assertEquals("jeu",
                dictionary.translateWord("jeu", "fr", "ro"));
    }
}
