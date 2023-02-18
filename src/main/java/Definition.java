import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Definition implements Comparable<Definition>{
    private String dict;
    private String dictType;
    private int year;
    private List<String> text;

    public Definition() {
        this.text = new ArrayList<>();
    }

    public Definition(String dict, String dictType, int year) {
        this.dict = dict;
        this.dictType = dictType;
        this.year = year;
    }

    public String getDict() {
        return dict;
    }
    public void addText(String string){
        this.text.add(string);
    }

    public String getDictType() {
        return dictType;
    }

    public List<String> getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Definition definition = (Definition) o;
        return year == definition.year && dict.equals(definition.dict) && dictType.equals(definition.dictType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dict, dictType, year, text);
    }

    @Override
    public String toString() {
        return "Definition{" +
                "dict='" + dict + '\'' +
                ", dictType='" + dictType + '\'' +
                ", year=" + year +
                //", text=" + text +
                '}';
    }

    @Override
    public int compareTo(Definition o) {
        if(o.year != this.year)
            return Integer.compare(this.year, o.year);
        return this.dict.compareTo(o.dict);
    }
}
