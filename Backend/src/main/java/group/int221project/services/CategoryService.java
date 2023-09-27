package group.int221project.services;
import group.int221project.entities.Category;
import group.int221project.repositories.CategoryRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer categoryId) {
        if (categoryId == 0) {
            throwConstraintViolationException("must not be null");
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throwConstraintViolationException("does not exists");
        }

        return null;
    }

    //create method to throw exception
    public void throwConstraintViolationException(String message) {
        throw new ConstraintViolationException(null, null) {
            @Override
            public Set<ConstraintViolation<?>> getConstraintViolations() {
                return Set.of(new ConstraintViolation<>() {
                    @Override
                    public String getMessage() {
                        return message;
                    }

                    @Override
                    public String getMessageTemplate() {
                        return null;
                    }

                    @Override
                    public Object getRootBean() {
                        return null;
                    }

                    @Override
                    public Class<Object> getRootBeanClass() {
                        return null;
                    }

                    @Override
                    public Object getLeafBean() {
                        return null;
                    }

                    @Override
                    public Object[] getExecutableParameters() {
                        return new Object[0];
                    }

                    @Override
                    public Object getExecutableReturnValue() {
                        return null;
                    }

                    @Override
                    public Path getPropertyPath() {
                        return PathImpl.createPathFromString("categoryId");
                    }

                    @Override
                    public Object getInvalidValue() {
                        return null;
                    }

                    @Override
                    public ConstraintDescriptor<?> getConstraintDescriptor() {
                        return null;
                    }

                    @Override
                    public <U> U unwrap(Class<U> aClass) {
                        return null;
                    }
                });
            }
        };
    }

    public Category addCategory(Category newCategory){
        return categoryRepository.saveAndFlush(newCategory);
    }
}
