import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testRemoveWord {
    static Dictionary dictionary;
    static {
        dictionary = new Dictionary();
    }
    @Test
    public void test1(){
        Assertions.assertTrue(dictionary.removeWord("pisică", "ro"));
    }
    @Test
    public void test2(){
        Word word = new Word("cheval", "horse", "noun");
        dictionary.addWord(word, "fr");
        Assertions.assertTrue(dictionary.removeWord("cheval", "fr"));
    }
    @Test
    public void test3(){
        Assertions.assertFalse(dictionary.removeWord("cal", "ro"));
    }
}
