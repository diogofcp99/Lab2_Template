package pt.pa.model;

import pt.pa.adts.Position;
import pt.pa.adts.Tree;
import pt.pa.adts.TreeLinked;

public class BookmarkManager {

    private Tree<BookmarkEntry> bookmarks;

    public BookmarkManager(){
        this.bookmarks = new TreeLinked<>(new BookmarkEntry("Bookmarks"));
    }

    private Position<BookmarkEntry> find(String key) {
        for(Position<BookmarkEntry> b : this.bookmarks.positions()){
            if(b.element().getKey().equalsIgnoreCase(key))
                return b;
        }
        return null;
    }

    public boolean exists(String key){
        return find(key) != null;
    }

    public void addBookmarkFolder(String keyParent, String keyFolder) throws BookmarkInvalidOperation{
        if (!exists(keyParent)) throw new BookmarkInvalidOperation("Parent Folder doesn't exist!");
        if(exists(keyFolder)) throw new BookmarkInvalidOperation("Folder already exist!");

        BookmarkEntry folder = new BookmarkEntry(keyFolder);
        Position<BookmarkEntry> parent = find(keyParent);
        bookmarks.insert(parent, folder);
    }

    public void addBookmarkEntry(String keyParent, String keyEntry, String url) throws BookmarkInvalidOperation{
        if (!exists(keyParent)) throw new BookmarkInvalidOperation("Parent Folder doesn't exist!");
        if(exists(keyEntry)) throw new BookmarkInvalidOperation("Entry already exist!");

        BookmarkEntry entry = new BookmarkEntry(keyEntry, url);
        Position<BookmarkEntry> parent = find(keyParent);
        bookmarks.insert(parent, entry);
    }

    public int getTotalEntries(){
        return this.bookmarks.size() - 1;
    }

    public String getParentFolder(String keyEntry) throws BookmarkInvalidOperation{
        if(!exists(keyEntry)) throw new BookmarkInvalidOperation("Entry doesn't exists!");
        if(find(keyEntry).element().isFolder()) throw new BookmarkInvalidOperation("Key is not an url.");

        Position<BookmarkEntry> parent = bookmarks.parent(find(keyEntry));
        return parent.element().getKey();

    }

    @Override
    public String toString(){
        return bookmarks.toString();
    }

    public int getTotalFolders(){
        int counter = 0;
        for(Position<BookmarkEntry> b : this.bookmarks.positions()){
            if(b.element().isFolder()) {
                counter++;
            }

        }
        return counter;
    }

    public String fullPathOf(String keyEntry) throws BookmarkInvalidOperation {
        Position<BookmarkEntry> position = find(keyEntry);
        if(bookmarks.isRoot(position)){
            return keyEntry;
        } else {
            return keyEntry + " -> " + fullPathOf(bookmarks.parent(position).element().getKey());
        }

    }
}
