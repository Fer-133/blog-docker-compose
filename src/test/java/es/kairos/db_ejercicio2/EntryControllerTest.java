package es.kairos.db_ejercicio2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.kairos.db_ejercicio2.entry.Entry;
import es.kairos.db_ejercicio2.entry.EntryController;
import es.kairos.db_ejercicio2.entry.EntryRepository;
import es.kairos.db_ejercicio2.comment.Comment;
import es.kairos.db_ejercicio2.comment.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EntryController.class)
class EntryControllerTest {
    @MockBean
    EntryRepository entryRepository;

    @MockBean
    CommentRepository commentRepository;

    @Autowired
    MockMvc mvc;

    @Test
    void testGetEntrys() throws Exception{

        when(entryRepository.findAll()).thenReturn(List.of());

        mvc.perform(get("/entrys/"))
                .andExpect(content().string("[]"))
                .andExpect(status().isOk());

    }

    @Test
    void testGetEntryNotFound() throws Exception {

        mvc.perform(get("/entrys/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testGetExistingEntry() throws Exception {

        String expectedEntry = getPostedEntry();

        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        entry.setId(1);
        Optional<Entry> optionalEntry = Optional.of(entry);

        when(entryRepository.findById(1L)).thenReturn(optionalEntry);

        mvc.perform(get("/entrys/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEntry));

    }


    @Test
    void testPostEntry() throws Exception {

        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        entry.setId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(entry);
        //String expectedEntry = ow.writeValueAsString(entry);

        when(entryRepository.save(any(Entry.class))).thenReturn(entry);

        mvc.perform(post("/entrys/").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestJson));

    }

    @Test
    void testDeleteEntryNotFound() throws Exception {

        mvc.perform(delete("/entrys/1"))
                .andExpect(status().isNotFound());

    }


    @Test
    void testDeleteExistingEntry() throws Exception {

        String expectedEntry = getPostedEntry();

        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        entry.setId(1);
        Optional<Entry> optionalEntry = Optional.of(entry);

        when(entryRepository.findById(1L)).thenReturn(optionalEntry);
        entryRepository.deleteById(1L);
        verify(entryRepository).deleteById(1L);

        mvc.perform(delete("/entrys/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEntry));
    }

    @Test
    void testUpdateEntryNotFound() throws Exception {

        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(entry);

        mvc.perform(put("/entrys/1").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isNotFound());

    }

    @Test
    void testUpdateExistingEntry() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        //Creo la entrada original
        Entry entry = new Entry ("asdf", "asdf", "asdf", "asdf", "asdf");
        //CREO EL OPTIONAL QUE LE VOY A PASAR A MOCKITO
        Optional<Entry> optionalEntry = Optional.of(entry);

        //Creo la entrada modificada
        Entry updatedEntry = new Entry ("a", "a", "a", "a", "a");
        String requestJson = ow.writeValueAsString(updatedEntry);

        when(entryRepository.findById(1L)).thenReturn(optionalEntry);
        when(entryRepository.save(any(Entry.class))).thenReturn(entry);

        mvc.perform(put("/entrys/1").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(requestJson));

    }



    @Test
    void testPostCommentNotFound() throws Exception {

        Comment comment = new Comment ();
        comment.setComment("asd");
        comment.setDate(null);
        comment.setAuthorNickname("asd");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(comment);

        comment.setId(2);
        String expectedEntry = ow.writeValueAsString(comment);

        mvc.perform(post("/entrys/1/comments").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isNotFound());

    }


    @Test
    void testPostCommentIsOk() throws Exception {

        //Creo el entry al que le voy añadir el comentario
        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        entry.setId(1);
        entry.setComments(new ArrayList<Comment>());

        //Creo el comentario
        Comment comment = new Comment ();
        comment.setComment("asd");
        comment.setDate(null);
        comment.setAuthorNickname("asd");
        comment.setId(2);

        //Con el comentario creo el body en JSON que voy a enviar en la peticion
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(comment);

        //Creo la entrada que voy a esperar como respuesta y le añado el comentario
        Entry updatedEntry = new Entry ("asd", "asd", "asd", "asd", "asd");
        updatedEntry.setId(1);
        updatedEntry.setComments(new ArrayList<Comment>());
        updatedEntry.getComments().add(comment);
        String expectedEntry = ow.writeValueAsString(updatedEntry);


        Optional<Entry> optionalEntry = Optional.of(entry);
        when(entryRepository.findById(1L)).thenReturn(optionalEntry);
        when(entryRepository.save(any(Entry.class))).thenReturn(entry);

        mvc.perform(post("/entrys/1/comments/").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedEntry));

    }

    @Test
    void testDeleteCommentNotFound() throws Exception {

        mvc.perform(delete("/entrys/1/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteExistingComment() throws Exception {

        //Creo el comentario que va a contener la entrada
        Comment comment = new Comment();
        comment.setId(2);

        //Creo el entry que tiene que buscar con el comentario ya introducido
        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        entry.setId(1);
        entry.setComments(new LinkedList<Comment>(Arrays.asList(comment)));

        //Creo la entrada sin comentario que voy a esperar como respuesta
        Entry entryWithoutComment = new Entry ("asd", "asd", "asd", "asd", "asd");
        entryWithoutComment.setId(1);
        entryWithoutComment.setComments(new ArrayList<Comment>());

        //Parseo la entrada esperada a json
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String expectedEntry = ow.writeValueAsString(entryWithoutComment);

        //Creo el optional de la entrada con comentario necesario para pasarselo a mockito
        Optional<Entry> optionalEntry = Optional.of(entry);

        when(entryRepository.findById(1L)).thenReturn(optionalEntry);
        when(entryRepository.save(any(Entry.class))).thenReturn(entry);

        commentRepository.deleteById(2L);
        verify(commentRepository).deleteById(2L);

        mvc.perform(delete("/entrys/1/comments/2"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEntry));

    }



    private String getPostedEntry() throws JsonProcessingException {
        Entry entry = new Entry ("asd", "asd", "asd", "asd", "asd");
        entry.setId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(entry);
    }
}