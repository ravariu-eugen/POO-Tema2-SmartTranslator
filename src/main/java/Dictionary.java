import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.*;

public class Dictionary {
    private final Map<WordID, Word> wordMap;
    public Dictionary(){
        wordMap = new HashMap<>();
        init("src/main/resources");
    }
    static class WordID{
        // folosit pentru a identifica un cuvant
        private final String language;
        private final String word;
        @Override
        public String toString() {
            return "WordID(" + language + ',' + word + ')';
        }

        public WordID(String word, String language) {
            this.language = language;
            this.word = word;

        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WordID wordID = (WordID) o;
            return language.equals(wordID.language) && word.equals(wordID.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(language, word);
        }
    }
    public Word getWord(String word, String language){
        if(word == null || language == null)
            throw new NullPointerException();
        return wordMap.get(new WordID(word, language));
    }
    public Word translateFromEn(String word_en, String language){
        for (Map.Entry<WordID, Word> entry: wordMap.entrySet()) {
            if(entry.getValue().getWord_en().equals(word_en) && entry.getKey().language.equals(language))
                return entry.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "wordMap=" + wordMap +
                '}';
    }

    public void init(String path){
        Gson gson = new Gson();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for(File dictionary: files){
                if(dictionary.getName().matches(".._dict.json")) {
                    try (FileReader reader = new FileReader(dictionary)) {
                    JsonReader jsonReader = new JsonReader(reader);
                    String language = dictionary.getName().substring(0, 2);
                    Word[] words = gson.fromJson(jsonReader, Word[].class);
                    for (Word word : words) {
                        addWord(word, language);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            }
        }
    }

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        System.out.println(dictionary);
        System.out.println(dictionary.getDefinitionsForWord("câine", "ro"));
        System.out.println(dictionary.translateWord("câine", "ro", "fr"));
        System.out.println(dictionary.translateSentence("l| pisici|mergem", "ro", "fr"));
        System.out.println(dictionary.translateSentences("l| pisici|câine", "ro", "fr"));

    }
    public boolean addWord(Word word, String language){
        WordID wordID = new WordID(word.getWord(), language);
        if(wordMap.containsKey(wordID))
            return false;
        wordMap.put(wordID,word);
        for(String singularForm :word.getSingular())
            wordMap.put(new WordID(singularForm, language), word);
        for(String pluralForm :word.getPlural())
            wordMap.put(new WordID(pluralForm, language), word);
        return true;
    }
    public boolean removeWord(String word, String language){
        WordID wordID = new WordID(word, language);
        if(!wordMap.containsKey(wordID))
            return false;
        Word word1 = wordMap.get(wordID);

        wordMap.remove(new WordID(word1.getWord(), language));
        for(String singularForm :word1.getSingular())
            wordMap.remove(new WordID(singularForm, language));
        for(String pluralForm :word1.getPlural())
            wordMap.remove(new WordID(pluralForm, language));
        return true;
    }
    public boolean addDefinitionForWord(String word, String language, Definition definition){
        Word word1 = getWord(word, language);
        if(word1 == null)
            return false;
        return word1.addDefinition(definition);
    }
    public boolean removeDefinitionForWord(String word, String language, String dictionary){
        Word word1 = getWord(word, language);
        if(word1 == null)return false;
        return word1.removeDefinition(dictionary);
    }
    public ArrayList<Definition> getDefinitionsForWord(String word, String language){
        Word word1 = getWord(word, language);
        if(word1 == null)
            return null;
        return new ArrayList<>(word1.getDefinitions());
    }
    /**     Traduce un cuvant din limba initiala in cea finala.
     *      Daca cuvantul nu poate fi tradus, intoarce cuvantul initial
     * @param word cuvantul de tradus
     * @param fromLanguage limba initiala
     * @param toLanguage limba finala
     * @return cuvantul tradus
     */
    public String translateWord(String word, String fromLanguage, String toLanguage){
        int conjugation = 0;
        if(fromLanguage.equals(toLanguage))
            return word;// aceeasi limba
        String wordEn; // traducerea in engleza a cuvantului word

        Word fromWord = getWord(word, fromLanguage);
        if(fromWord == null) // nu exista cuvantul in limba initiala
            return word;
        conjugation = fromWord.getConjugation(word);
        wordEn = fromWord.getWord_en();

        Word toWord = translateFromEn(wordEn, toLanguage);
        if (toWord == null) // nu exista cuvantul in limba finala
            return word;
        return toWord.getForm(conjugation);

    }


    public String translateSentence(String sentence, String fromLanguage, String toLanguage){
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> words = new ArrayList<>(
                List.of(sentence.split("[^\\p{L}-]+")));// gasim cuvintele
        if(words.get(0).equals(""))
            words.remove(0);
        // gasim secventele de caractere dintre cuvinte
        ArrayList<String> betweenWords = new ArrayList<>(
                List.of(sentence.split("[\\p{L}-]+")));
        if(betweenWords.get(0).equals(""))
            betweenWords.remove(0);
        boolean hasPrefix = sentence.matches("^[^\\p{L}-].+");
        // daca propozitia incepe cu caracter ce nu este litera
        boolean hasSuffix = sentence.matches(".+[^\\p{L}-]$");
        // daca propozitia se termina cu caracter ce nu este litera

        int betweenIndex = 0;
        int wordIndex = 0;
        if(hasPrefix)
            stringBuilder.append(betweenWords.get(betweenIndex++));
        for (; wordIndex<words.size()-1; wordIndex++,betweenIndex++) {
            stringBuilder.append(translateWord(words.get(wordIndex), fromLanguage, toLanguage));
            stringBuilder.append(betweenWords.get(betweenIndex));
        }
        stringBuilder.append(translateWord(words.get(wordIndex), fromLanguage, toLanguage));
        if(hasSuffix)
            stringBuilder.append(betweenWords.get(betweenIndex));
        return stringBuilder.toString();
    }
    public ArrayList<String> getSynonyms(String word, String language){
        Word word1 = getWord(word, language);
        if(word1 == null) {
            ArrayList<String> synonyms = new ArrayList<>();
            synonyms.add(word);
            return synonyms;
        }
        Set<Definition> definitions = word1.getDefinitions();
        Set<String> synonyms = new HashSet<>();
        for (Definition definition: definitions){
            if(definition.getDictType().equals("synonyms")){
                synonyms.addAll(definition.getText());
            }
        }
        return new ArrayList<>(synonyms);
    }
    public ArrayList<String> translateSentences(String sentence, String fromLanguage, String toLanguage){
        ArrayList<String> sentences = new ArrayList<>();

        ArrayList<String> words = new ArrayList<>(
                List.of(sentence.split("[^\\p{L}-]+")));// gasim cuvintele
        if(words.get(0).equals(""))
            words.remove(0);
        // gasim secventele de caractere dintre cuvinte
        ArrayList<String> betweenWords = new ArrayList<>(
                List.of(sentence.split("[\\p{L}-]+")));
        if(betweenWords.get(0).equals(""))
            betweenWords.remove(0);
        boolean hasPrefix = sentence.matches("^[^\\p{L}-].+");
        // daca propozitia incepe cu caracter ce nu este litera
        boolean hasSuffix = sentence.matches(".+[^\\p{L}-]$");
        // daca propozitia se termina cu caracter ce nu este litera




        ArrayList<ArrayList<String>> synonymLists = new ArrayList<>(words.size());
        for(String word:words){
            String translatedWord = translateWord(word, fromLanguage, toLanguage);
            ArrayList<String> synonims = getSynonyms(translatedWord, toLanguage);
            if(synonims.isEmpty())
                synonims.add(translatedWord);
            synonymLists.add(synonims);
        }

        int maxNumberOfSentences = 7;
        ArrayList<Integer> currentSentence = new ArrayList<>(words.size());
        ArrayList<Integer> numberOfSynonyms = new ArrayList<>(words.size());

        for(ArrayList<String> synonymList:synonymLists){
            currentSentence.add(0);
            numberOfSynonyms.add(synonymList.size());
        }
        System.out.println(words);
        System.out.println(numberOfSynonyms);
        while (sentences.size() < maxNumberOfSentences){
            StringBuilder stringBuilder = new StringBuilder();
            int betweenIndex = 0;
            int wordIndex = 0;
            if(hasPrefix)
                stringBuilder.append(betweenWords.get(betweenIndex++));
            for (; wordIndex<words.size()-1; wordIndex++,betweenIndex++) {
                ArrayList<String> synonyms = synonymLists.get(wordIndex);
                String currentSynonym = synonyms.get(currentSentence.get(wordIndex));
                stringBuilder.append(currentSynonym);
                stringBuilder.append(betweenWords.get(betweenIndex));
            }
            stringBuilder.append(translateWord(words.get(wordIndex), fromLanguage, toLanguage));
            if(hasSuffix)
                stringBuilder.append(betweenWords.get(betweenIndex));
            nextArrangement(currentSentence, numberOfSynonyms);
            sentences.add(stringBuilder.toString());
        }
        return sentences;
    }

    private int nextArrangement(ArrayList<Integer> current, ArrayList<Integer> max) {
        int position = 0;
        System.out.println(current);
        while (true){
            int next = current.get(position) + 1;
            if(next == max.get(position)){
                current.set(position, 0);
                position++;
                if(position == max.size())
                    return 1;
                continue;
            }
            current.set(position, next);
            return 0;
        }
    }
}


