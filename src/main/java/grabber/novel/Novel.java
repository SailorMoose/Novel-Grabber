package grabber.novel;

import system.Logger;

import java.util.*;

public class Novel {
    private final NovelMetadata metadata;
    private NovelOptions options = new NovelOptions();
    private final Map<String, byte[]> images = new HashMap<>();
    private int wordCount = 0;

    public Novel(NovelMetadata metadata) {
        this.metadata = metadata;
    }

    public void addWordCount(int wordsToAdd) {
        this.wordCount += wordsToAdd;
    }

    public int getWordCount() {
        return wordCount;
    }

    public NovelMetadata getMetadata() {
        return metadata;
    }

    public List<Chapter> getChapters() {
        return metadata.getChapterList();
    }

    public void setChapters(List<Chapter> chapters) {
        metadata.setChapterList(chapters);
    }

    public Map<String, byte[]> getImages() {
        return images;
    }

    public NovelOptions getOptions() {
        return options;
    }

    public void setOptions(NovelOptions options) {
        this.options = options;
        // Replace 'all chapters' (-1) variable with actual last chapter number
        if (options.getLastChapter() == -1) {
            int actualLastChapter = metadata.getChapterList().size();
            NovelOptions.modifier(options)
                    .endingChapterIndex(actualLastChapter)
                    .build();
        }
    }

    public List<Chapter> getFailedChapters() {
        List<Chapter> failedChapters = new ArrayList<>();
        for (Chapter chapter :  metadata.getChapterList()) {
            if (chapter.getDownloadStatus() == Chapter.Status.FAILED) failedChapters.add(chapter);
        }
        return failedChapters;
    }

    public List<Chapter> getToDownloadChapters() {
        int fromIndex = options.getFirstChapter();
        int toIndex = options.getLastChapter();
        Logger.info(String.format("Start %d, End %d, Size %d", fromIndex, toIndex, metadata.getChapterList().size()));
        if (fromIndex > toIndex) {
            fromIndex = options.getLastChapter();
            toIndex = options.getFirstChapter();
        }
        Logger.info(String.format("Start %d, End %d", fromIndex, toIndex));
        // subList toIndex is exclusive
        return metadata.getChapterList().subList(fromIndex, toIndex+1);
    }

    public int getChaptersToDownloadCount() {
        return getToDownloadChapters().size();
    }

    public void reverseChapterOrder() {
        Collections.reverse(metadata.getChapterList());
    }
}