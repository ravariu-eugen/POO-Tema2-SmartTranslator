import java.util.*;

public class Word {
    private String word, word_en;
    private String type;
    private String[] singular, plural;
    private SortedSet<Definition> definitions;

    public Word() {
        definitions = new TreeSet<>();
    }

    public Word(String word, String word_en, String type) {
        this.word = word;
        this.word_en = word_en;
        this.type = type;
    }

    public Word(String word, String word_en, String type, String[] singular, String[] plural, SortedSet<Definition> definitions) {
        this.word = word;
        this.word_en = word_en;
        this.type = type;
        this.singular = singular;
        this.plural = plural;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public String getWord_en() {
        return word_en;
    }

    public String[] getSingular() {
        return singular;
    }

    public String[] getPlural() {
        return plural;
    }

    public boolean addDefinition(Definition definition){
        if (definition == null)
            throw new NullPointerException();
        return definitions.add(definition);
    }
    public Definition getDefinition(String dict){
        for(Definition definition:definitions){
            if(definition.getDict().equals(dict))
                return definition;
        }
        return null;
    }

    public Set<Definition> getDefinitions() {
        return definitions;
    }

    public boolean removeDefinition(String dict){
        Definition definition = getDefinition(dict);
        if(definition == null)
            return false;
        return definitions.remove(definition);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return word.equals(word1.word);
    }
    int getConjugation(String form){
        if(form.equals(word))
            return 0;
        if(type.equals("noun")){
            if(form.equals(singular[0]))
                return 1;
            if(form.equals(plural[0]))
                return 2;
        }
        else if(type.equals("verb")){
            if(form.equals(singular[0]))
                return 1;
            if(form.equals(singular[1]))
                return 2;
            if(form.equals(singular[2]))
                return 3;
            if(form.equals(plural[0]))
                return 4;
            if(form.equals(plural[1]))
                return 4;
            if(form.equals(plural[2]))
                return 4;
        }
        return -1;
    }
    String getForm(int conjugation){
        if(type.equals("noun")){
            return switch (conjugation) {
                case 0 -> word;
                case 1 -> singular[0];
                case 2 -> plural[0];
                default -> null;
            };
        }else if(type.equals("verb")){
            return switch (conjugation){
                case 0 -> word;
                case 1 -> singular[0];
                case 2 -> singular[1];
                case 3 -> singular[2];
                case 4 -> plural[0];
                case 5 -> plural[1];
                case 6 -> plural[2];
                default -> null;
            };
        }
        return null;
    }
    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", word_en='" + word_en + '\'' +
                ", type='" + type + '\'' +
                ", singular=" + Arrays.toString(singular) +
                ", plural=" + Arrays.toString(plural) +
                ", definitions=" + definitions +
                '}' + '\n';
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(word, word_en, type, definitions);
        result = 31 * result + Arrays.hashCode(singular);
        result = 31 * result + Arrays.hashCode(plural);
        return result;
    }
}
