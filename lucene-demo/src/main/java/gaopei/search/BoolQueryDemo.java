package gaopei.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * BoolQuery
 *
 * @author pei.gao
 */
public class BoolQueryDemo {
    private Directory directory;
    private IndexWriter indexWriter;

    {
        try {
            directory = new RAMDirectory();
            Analyzer analyzer = new WhitespaceAnalyzer();
            IndexWriterConfig conf = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void index() throws Exception {
        Document doc;
        // 0
        doc = new Document();
        doc.add(new TextField("content", "h", Field.Store.YES));
        doc.add(new TextField("author", "author1", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 1
        doc = new Document();
        doc.add(new TextField("content", "b", Field.Store.YES));
        doc.add(new TextField("author", "author2", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 2
        doc = new Document();
        doc.add(new TextField("content", "a c", Field.Store.YES));
        doc.add(new TextField("author", "author3", Field.Store.YES));

        indexWriter.addDocument(doc);
        // 3
        doc = new Document();
        doc.add(new TextField("content", "a c e", Field.Store.YES));
        doc.add(new TextField("author", "author4", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 4
        doc = new Document();
        doc.add(new TextField("content", "h", Field.Store.YES));
        doc.add(new TextField("author", "author5", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 5
        doc = new Document();
        doc.add(new TextField("content", "c e", Field.Store.YES));
        doc.add(new TextField("author", "author6", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 6
        doc = new Document();
        doc.add(new TextField("content", "c a e", Field.Store.YES));
        doc.add(new TextField("author", "author7", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 7
        doc = new Document();
        doc.add(new TextField("content", "f", Field.Store.YES));
        doc.add(new TextField("author", "author8", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 8
        doc = new Document();
        doc.add(new TextField("content", "b c d e c e", Field.Store.YES));
        doc.add(new TextField("author", "author9", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 9
        doc = new Document();
        doc.add(new TextField("content", "a c e a b c", Field.Store.YES));
        doc.add(new TextField("author", "author10", Field.Store.YES));
        indexWriter.addDocument(doc);
        indexWriter.commit();
        // 索引阶段结束
    }

    private void searchMust() throws Exception {
        System.out.println("searchMust");
        //查询阶段
        IndexReader reader = DirectoryReader.open(indexWriter);
        IndexSearcher searcher = new IndexSearcher(reader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("content", "a")), BooleanClause.Occur.MUST);
        builder.add(new TermQuery(new Term("content", "b")), BooleanClause.Occur.MUST);
        builder.add(new TermQuery(new Term("content", "c")), BooleanClause.Occur.MUST);
        builder.add(new TermQuery(new Term("content", "e")), BooleanClause.Occur.MUST);

        int resultTopN = 7;

        ScoreDoc[] scoreDocs = searcher.search(builder.build(), resultTopN).scoreDocs;

        System.out.println("Total Result Number: " + scoreDocs.length + "");
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 输出满足查询条件的 文档号
            System.out.println("result" + i + ": 文档" + scoreDoc.doc + "");
        }

    }

    private void searchShould() throws Exception {
        System.out.println("searchShould");

        //查询阶段
        IndexReader reader = DirectoryReader.open(indexWriter);
        IndexSearcher searcher = new IndexSearcher(reader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("content", "a")), BooleanClause.Occur.SHOULD);
        builder.add(new TermQuery(new Term("content", "b")), BooleanClause.Occur.SHOULD);

        int resultTopN = 7;

        ScoreDoc[] scoreDocs = searcher.search(builder.build(), resultTopN).scoreDocs;

        System.out.println("Total Result Number: " + scoreDocs.length + "");
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 输出满足查询条件的 文档号
            System.out.println("result" + i + ": 文档" + scoreDoc.doc + "");
        }

    }

    private void searchMinShouldMatch() throws Exception {
        System.out.println("searchMinShouldMatch");

        //查询阶段
        IndexReader reader = DirectoryReader.open(indexWriter);
        IndexSearcher searcher = new IndexSearcher(reader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("content", "a")), BooleanClause.Occur.SHOULD);
        builder.add(new TermQuery(new Term("content", "b")), BooleanClause.Occur.SHOULD);
        builder.setMinimumNumberShouldMatch(2);

        int resultTopN = 7;

        ScoreDoc[] scoreDocs = searcher.search(builder.build(), resultTopN).scoreDocs;

        System.out.println("Total Result Number: " + scoreDocs.length + "");
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 输出满足查询条件的 文档号
            System.out.println("result" + i + ": 文档" + scoreDoc.doc + "");
        }
    }

    /**
     * todo
     * @throws Exception
     */
    private void searchNot() throws Exception {
        System.out.println("searchNot");

        //查询阶段
        IndexReader reader = DirectoryReader.open(indexWriter);
        IndexSearcher searcher = new IndexSearcher(reader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("content", "")), BooleanClause.Occur.MUST_NOT);

        int resultTopN = 7;

        ScoreDoc[] scoreDocs = searcher.search(builder.build(), resultTopN).scoreDocs;

        System.out.println("Total Result Number: " + scoreDocs.length + "");
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 输出满足查询条件的 文档号
            System.out.println("result" + i + ": 文档" + scoreDoc.doc + "");
        }
    }

    public static void main(String[] args) throws Exception {
        BoolQueryDemo termQueryTest = new BoolQueryDemo();
        termQueryTest.index();
        termQueryTest.searchMust();
        termQueryTest.searchShould();
        termQueryTest.searchMinShouldMatch();
        termQueryTest.searchNot();

    }
}
