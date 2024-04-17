package com.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileFilter;


import java.nio.file.Path;
import java.nio.file.Paths;


public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        Path indexPath = Paths.get(indexDirectoryPath);
        Directory indexDirectory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        writer = new IndexWriter(indexDirectory, config);
    }

    public void close() throws IOException {
        writer.close();
    }

    public void indexFile(File file) throws IOException {
        if (file.isDirectory() || file.isHidden() || !file.exists() || !file.canRead()) {
            return; // Skip directories, hidden files, non-existent files, or unreadable files
        }

        System.out.println(" Indexing file: " + file.getCanonicalPath());
        Document document = createDocument(file);
        writer.addDocument(document);
    }

    private Document createDocument(File file) throws IOException {
        Document document = new Document();

        // Index file contents (use TextField for indexing text content)
        TextField contentField = new TextField(LuceneConstants.CONTENTS, new FileReader(file));
        document.add(contentField);

        // Store and index file name
        document.add(new TextField(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES));

        // Store and index file path
        document.add(new TextField(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES));

        return document;
    }

    public int createIndex(String dataDirPath, FileFilter filter) throws IOException {
        File[] files = new File(dataDirPath).listFiles(filter);
        int numIndexed = 0;

        if (files != null) {
            for (File file : files) {
                indexFile(file);
                numIndexed++;
            }
        }

        return numIndexed;
    }
}
