package com.example;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {
    private final IndexSearcher indexSearcher;
    private final QueryParser queryParser;

    public Searcher(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(indexReader);
        queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
    }

public TopDocs search(String searchQuery) throws IOException, ParseException {
    Query query = queryParser.parse(searchQuery);
    return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
}

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException {
        indexSearcher.getIndexReader().close();
    }
}
