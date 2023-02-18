import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testAddDefinition {
    static Dictionary dictionary;
    static {
        dictionary = new Dictionary();
    }
    @Test
    public void test1(){

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
