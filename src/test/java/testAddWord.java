import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testAddWord {
    static Dictionary dictionary;
    static {
        dictionary = new Dictionary();
    }
    @Test
    public void test1(){
        Word word = new Word("cal", "horse", "noun");
        Assertions.assertTrue(dictionary.addWord(word, "ro"));
    }
    @Test
    public void test2(){
        Word word = new Word("pisică", "cat", "noun");
        Assertions.assertTrue(dictionary.addWord(word, "fr"));
    }
    @Test
    public void test3(){
        Word word = new Word("pisică", "cat", "noun");
        Assertions.assertFalse(dictionary.addWord(word, "ro"));
    }
}
