package cat.copernic.mavenproject1.apiControllers;

import cat.copernic.mavenproject1.Entity.Ad;
import cat.copernic.mavenproject1.Entity.Category;
import cat.copernic.mavenproject1.logic.CategoryLogic;
import java.nio.file.Files;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rest/categories")
public class CategoryApiController {

    @Autowired
    private CategoryLogic categoryLogic;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> findAll() {
        List<Category> categories = categoryLogic.findAllCategories();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
            return new ResponseEntity<>(categories, headers, HttpStatus.OK);
        
    }

    @GetMapping("/byId/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryLogic.getCategoryById(categoryId);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/proposals")
    public ResponseEntity<List<Category>> getAllProposals() {
        List<Category> proposals = categoryLogic.findAllProposals();
        if (proposals != null) {
            return new ResponseEntity<>(proposals, HttpStatus.OK);
        }else if (proposals.isEmpty()){
            
             return new ResponseEntity<>(proposals, HttpStatus.NO_CONTENT);
            
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<Long> createCategory(@RequestParam String name, @RequestParam String description, @RequestParam boolean proposal, @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        byte[] img = null;
        Category category = new Category(name, description, img, proposal, new ArrayList<>());
        
        Long categoryId = categoryLogic.saveCategory(category, imageFile);
        return new ResponseEntity<>(categoryId, HttpStatus.CREATED);
    }
    
    @PutMapping("/acceptProposal/{categoryId}")
    public ResponseEntity<Void> acceptProposal(@PathVariable Long categoryId){
                
        ResponseEntity<Void> response;
        
        try {
            if (categoryLogic.existsById(categoryId))
            {
                categoryLogic.acceptProposal(categoryId);
                response = ResponseEntity.noContent().build();
            }
            else
                response = ResponseEntity.notFound().build();

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
        return response;
    }
    
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long categoryId) {
        if (categoryLogic.existsById(categoryId)) {
            categoryLogic.deleteCategoryById(categoryId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateCategory(@RequestParam Long id, @RequestParam String name, @RequestParam String description, @RequestParam boolean proposal, @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        byte[] img = null;
        Category category = new Category(id, name, description, img, proposal, new ArrayList<>());
        if (category.getId() == null || !categoryLogic.existsById(category.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryLogic.updateCategory(category,imageFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
