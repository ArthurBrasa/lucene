import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneSearchingExample {
    public static void main(String[] args) {
        // Diretório onde o índice está armazenado (mesmo diretório usado na criação do índice)
        String indexPath = "index";

        // Configurar o analisador de texto (usaremos o StandardAnalyzer, mesmo utilizado na criação do índice)
        Analyzer analyzer = new StandardAnalyzer();

        // Consulta de pesquisa (termo que estamos procurando nos documentos)
        String queryString = "A";

        try {
            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));

            // Configurar o leitor do índice
            try (IndexReader indexReader = DirectoryReader.open(indexDirectory)) {
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);

                // Criar um parser de consulta
                QueryParser parser = new QueryParser("content", analyzer);

                // Criar uma consulta para o termo de busca especificado
                Query query;
                try {
                    query = parser.parse(queryString);
                } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                    System.err.println("Erro ao criar a consulta: " + e.getMessage());
                    return;
                }

                // Executar a pesquisa
                TopDocs results = indexSearcher.search(query, 10);

                // Exibir os resultados
                System.out.println("Total de documentos encontrados: " + results.totalHits);
                for (ScoreDoc scoreDoc : results.scoreDocs) {
                    Document doc = indexSearcher.doc(scoreDoc.doc);
                    System.out.println("ID: " + doc.get("filename") + ", Conteúdo: " + doc.get("content"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
