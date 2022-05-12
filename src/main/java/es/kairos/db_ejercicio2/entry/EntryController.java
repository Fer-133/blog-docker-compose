package es.kairos.db_ejercicio2.entry;

import es.kairos.db_ejercicio2.comment.Comment;
import es.kairos.db_ejercicio2.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entrys")
public class EntryController {

    @Autowired
    private EntryRepository repository;

    @Autowired
    private CommentRepository commentRepository;
/*
    @PostConstruct
    public void init() {
        repository.save(new Entry("Author 1", "a1", "Entrada uno", "Una intro", "sdfasdf"));
        repository.save(new Entry("Author 2", "a2", "Entrada 2", "Una intro", "sdfasdf"));
        repository.save(new Entry("Author 3", "a3", "Entrada 3", "Una intro", "sdfasdf"));
    }
*/
    @GetMapping("/")
    public List<Entry> entrys() {

        return repository.findAll();

    }

    @GetMapping("{id}")
    public Entry seeEntry(@PathVariable long id) {
        Entry entry = repository.findById(id).get();
        return entry;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Entry createEntry(@RequestBody Entry entry) {
        return repository.save(entry);
/*
        Entry updatedEntry = repository.save(entry);

        return updatedEntry;
 */
    }

    @DeleteMapping("{id}")
    public Entry deleteEntry(@PathVariable long id) {
/*
        repository.findById(id).get();
        repository.deleteById(id);
*/
        Entry entry = repository.findById(id).get();
        repository.deleteById(id);
        return entry;

    }

/*
    @PutMapping("/{id}")
    public ResponseEntity<Entry> updateEntry(@PathVariable long id, @RequestBody Entry updatedEntry) {
        Optional<Entry> saved = repository.findById(id);

        if(!saved.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
*/


    @PutMapping("/{id}")
    public Entry updateEntry(@PathVariable long id, @RequestBody Entry updatedEntry) {

        Entry entry = repository.findById(id).get();
        entry.setName(updatedEntry.getName());
        //entry.setComments(updatedEntry.getComments());
        entry.setNickname(updatedEntry.getNickname());
        entry.setIntro(updatedEntry.getIntro());
        entry.setContent(updatedEntry.getContent());
        entry.setTitle(updatedEntry.getTitle());

        updatedEntry = repository.save(entry);

        return updatedEntry;

    }

    @PostMapping("/{id}/comments/")
    public ResponseEntity<Entry> addComment(@PathVariable long id, @RequestBody Comment comment) {
        Optional <Entry> found = repository.findById(id);

        if(!found.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        found.get().getComments().add(comment);
        Entry saved = repository.save(found.get());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}/comments/{idComment}")
    public ResponseEntity<Entry> deleteComment(@PathVariable long id, @PathVariable long idComment) {
        Optional<Entry> found = repository.findById(id);

        if(found.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean deleted = false;
        Iterator<Comment> itComment = found.get().getComments().iterator();
        while(itComment.hasNext()) {
            if(itComment.next().getId() == idComment) {
                itComment.remove();
                deleted = true;
            }
        }

        if(!deleted){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Entry updated = repository.save(found.get());
        commentRepository.deleteById(idComment);

        return ResponseEntity.ok(updated);
    }

}
