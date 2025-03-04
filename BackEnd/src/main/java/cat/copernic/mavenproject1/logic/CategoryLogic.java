package cat.copernic.mavenproject1.logic;

import cat.copernic.mavenproject1.Entity.Category;
import cat.copernic.mavenproject1.repository.CategoryRepo;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryLogic {
    
    @Autowired
    private CategoryRepo categoryRepo;
    
    public List<Category> findAllCategories() {
        List<Category> categories = null;
        try{
            categories = categoryRepo.findAll();
            return categories;
        }catch(Exception e){
            throw new RuntimeException("Error al obtenir les categories", e);
        }
        
    }
    
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }
     
    public List<Category> findAllProposals() {
        List<Category> propostes = null;
        try{
        propostes = categoryRepo.findByProposalTrue();
        return propostes;
        }catch(Exception e){
            
            throw new RuntimeException("Error al obtenir les propostes de categories", e);
            
        }
        
    }
    
    public Category getCategoryByName(String name) {
        try{
            return categoryRepo.findByName(name);
        }catch(Exception e){
            return null;
        }
        
    }
    
    public boolean existsById(Long id) {
        return categoryRepo.existsById(id);
    }
    
    public void deleteCategoryById(Long id) {
        categoryRepo.deleteById(id);
    }
    
    public void acceptProposal(Long id) {
        Category proposta = getCategoryById(id);
        proposta.setProposal(false);
        categoryRepo.save(proposta);
        
        
    }
    
    public Long saveCategory(Category category, MultipartFile imageFile) {
        try{
            if (imageFile != null && !imageFile.isEmpty()) {
                category.setImage(convertImageToBlob(imageFile));
            }
           Category c =  categoryRepo.save(category);
           
        return c.getId();
        }catch(Exception e){
            return null;
        }
    }
    
    public Long updateCategory(Category category, MultipartFile imageFile) {
        try{
        Category oldCategory = getCategoryById(category.getId());
        byte[] img = convertImageToBlob(imageFile);
        oldCategory.setDescription(category.getDescription());
        oldCategory.setImage(img);
        oldCategory.setName(category.getName());
        oldCategory.setProposal(category.isProposal());
         
        
        categoryRepo.save(oldCategory);
        
        Category cat = getCategoryById(oldCategory.getId());
            System.out.println(" id: "+cat.getId() +" name: "+cat.getName()+ " descr: "+cat.getDescription());
        return oldCategory.getId();
        }catch(Exception e){
            return null;
        }
    }
    public byte[] convertImageToBlob(MultipartFile file)  {
        try{
        return file.getBytes();
        }catch(IOException e){
            return null;
        }
    }
}
