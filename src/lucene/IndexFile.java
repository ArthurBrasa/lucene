
package lucene;
import configs.GlobalConfigs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.sr.SerbianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author usuario1
 */
public class IndexFile {
    Directory directory;  
    File directory_data;
    Analyzer analyzer;
    private final ArrayList<File> files = new ArrayList<>();
    
    /**
     * @param files
     * @throws java.io.IOException
     */
    public IndexFile() throws IOException {
        loadArquivos(files);
    }
    
    public void makeIndex() throws IOException{
        // Criação do diretorio em disco 
        directory = makeIndexPath();
        
        // lendo arquivos 
        loadArquivos(files);
        
        // Criando Analyzer 
        analyzer = definindoAnalyzer(files);
        
        
//        IndexWriter indexWriter = new IndexWriter(directory, config);
//        for(File file : files){
//            System.out.println(file.getAbsoluteFile());
//            addDocument(indexWriter, file.getName(), getConteudo(file));
//        }
//        
//        System.out.println("Index SUCCESS!");
//        
        try {
//            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));

            // Configurar o escritor do índice
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            try (IndexWriter indexWriter_ = new IndexWriter(directory, config)) {

                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            // Ler o conteúdo do arquivo
                            String content = getConteudo(file);

                            // Criar um documento para o arquivo e adicioná-lo ao índice
                            addDocument(indexWriter_, file.getName(), content);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        
           
    }
    
    public Analyzer definindoAnalyzer(ArrayList<File> files){
        analyzer = new StandardAnalyzer();
        
        return analyzer;
    }
    
    private static void addDocument(IndexWriter indexWriter, String filename, String content) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("filename", filename, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        indexWriter.addDocument(doc);
    }
    
    public String getConteudo(File path) throws FileNotFoundException, IOException {
       // leitura dos conteudos dos aquivos
        FileReader fileReader = new FileReader(path);
        String content;
        try (BufferedReader burBufferedReader = new BufferedReader(fileReader)) {
            String line;
            content = new String();
            while ((line = burBufferedReader.readLine()) != null ) {
                content += line + " ";
            }
        }
        
        return content;
    }
    
    public Directory makeIndexPath() throws IOException{
        return FSDirectory.open(Paths.get(GlobalConfigs.INDEX_PATH));
    }
    
    public File loadArquivos(ArrayList<File> files) {
        // Leitura dos arquivos 
        directory_data = new File(GlobalConfigs.DATA_PATH);
        
        for ( File file : directory_data.listFiles()){
            System.err.println(file);
            files.add(file);
        }
        
        return directory_data;
    }
}
