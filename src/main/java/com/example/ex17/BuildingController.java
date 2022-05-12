package com.example.ex17;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/building")
@RequiredArgsConstructor
public class BuildingController {

    private final SessionFactory sf;
    private Session ss;
    @PostConstruct
    void init() {
        ss = sf.openSession();
    }

    // return a list of all
    @GetMapping
    public ResponseEntity<List<Building>> getAll() {
        Query<Building> q = ss.createQuery("from Building", Building.class);

        return new ResponseEntity<>(q.list(), HttpStatus.OK);
    }

    // return a single item
    @GetMapping({"/{id}"})
    public ResponseEntity<Building> getById(@PathVariable final long id) {

        Building a = ss.get(Building.class, id);

        if(a == null) {
            return new ResponseEntity<>(a, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(a, HttpStatus.OK);
        }
        
        
    }

    // filter by zip
    @GetMapping("/fortype")
    public ResponseEntity<List<Building>> filterByZip(@RequestParam String type) {
        CriteriaBuilder cb = ss.getCriteriaBuilder();
        CriteriaQuery<Building> cq = cb.createQuery(Building.class);
        Root<Building> r = cq.from(Building.class);
        cq.select(r).where(cb.equal(r.get("type"),type));
        
        Query<Building> q = ss.createQuery(cq);
        return new ResponseEntity<>(q.list(), HttpStatus.OK);
    }

    // filter by text - to pass wildcard (%) use '%25' in URL
    @GetMapping("/fordate")
    public ResponseEntity<List<Building>> filterByText(@RequestParam String date) {
        CriteriaBuilder cb = ss.getCriteriaBuilder();
        CriteriaQuery<Building> cq = cb.createQuery(Building.class);
        Root<Building> r = cq.from(Building.class);
        cq.select(r).where(cb.like(r.get("creationDate"),date));
        
        Query<Building> q = ss.createQuery(cq);
        return new ResponseEntity<>(q.list(), HttpStatus.OK);
    }

    // creates a new item and saves it to the DB and returns the count of created items
    @PostMapping
    public ResponseEntity<Building> create(@RequestBody Building a) {
        Transaction t = ss.beginTransaction();
        ss.persist(a);
        t.commit();
        ss.refresh(a);

        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    // deletes the specified Id and returns the count of deleted items
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Building> delete(@PathVariable("id") Long id) {
        Building a = ss.get(Building.class, id);

        if(a == null) {
            return new ResponseEntity<>(a, HttpStatus.NOT_FOUND);
        } else {
            Transaction t = ss.beginTransaction();
            ss.delete(a);
            t.commit();
            return new ResponseEntity<>(a, HttpStatus.OK);
        }
    }

    // updates the specified Id and returns the count of updated items
    @PutMapping({"/{id}"})
    public ResponseEntity<Building> edit(@PathVariable("id") Long id, @RequestBody Building changed) {
        Building a = ss.get(Building.class, id);

        if(a == null) {
            return new ResponseEntity<>(a, HttpStatus.NOT_FOUND);
        } else {
            a.setType(changed.getType());
            a.setCreationDate(changed.getCreationDate());
            Transaction t = ss.beginTransaction();
            ss.flush(); // persistent object already changed
            t.commit();
            return new ResponseEntity<>(a, HttpStatus.OK);
        }

    }

}
