package ru.zeet.task2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zeet.task2.entity.ProductSite;
import ru.zeet.task2.entity.Product;
import ru.zeet.task2.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    public Product findById(Integer productId) {
        return repository.findById(productId).orElse(null);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public List<Product> findAllByParentId(Integer parentId) {
        return repository.findAllByParentid(parentId);
    }


    private void setParentChild(Integer pId) {
        Optional<Product> optionalProduct = repository.findById(pId);
        if (optionalProduct.isPresent()) {
            Product parent = (Product) optionalProduct.get();
            parent.setChildren(isChild(parent));
            repository.save(parent);
        }
    }


    private boolean isDigit(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ProductSite addSite(ProductSite productSite) {
        // Есть ли запись в базе
        //Optional<Product> optionalProduct = repository.findById(productSite.getId());
        //if (!optionalProduct.isPresent()) {
        Product productDb = new Product();
        productDb.setText(productSite.getText());
        // Может придти # если кортневой элемент
        if (isDigit(productSite.getParentid()) && Integer.parseInt(productSite.getParentid()) != 0) {
            productDb.setParentid(Integer.parseInt(productSite.getParentid()));
        }
        productDb.setChildren(false);
        repository.save(productDb);
        productSite.setId(productDb.getId().toString());
        // productSite.setChildren("false");

        if (productDb.getParentid() != null) {
            setParentChild(productDb.getParentid());
        }

        return productSite;
    }

    public ProductSite updateSite(ProductSite productSite) {
        Integer oldParentId = -1;
        Optional<Product> optionalProduct = repository.findById(Integer.parseInt(productSite.getId()));
        if (optionalProduct.isPresent()) {
            Product productDb = optionalProduct.get();

            productDb.setText(productSite.getText());
            // Старый парент, на случай перемещения
            oldParentId = productDb.getParentid();
            // Может придти # если кортневой элемент
            if (isDigit(productSite.getParentid())) {
                productDb.setParentid(Integer.parseInt(productSite.getParentid()));
            } else {
                productDb.setParentid(null);
            }
            // productDb.setChildren(checkChildren(p));
            repository.save(productDb);
            // productSite.setChildren(productDb.isChildren()?"true":"false");

            if (productDb.getParentid() != null) {
                setParentChild(productDb.getParentid());
            }

            if (oldParentId != productDb.getParentid()) {
                setParentChild(oldParentId);
            }

            return productSite;
        }
        return null;
    }


    public void deleteSite(ProductSite productSite) {
        Product parent = null;

        Product product = null;
        product = repository.findById(Integer.parseInt(productSite.getId())).get();

        if (product.getParentid() != null) {
            Optional<Product> optionalProduct = repository.findById(product.getParentid());
            if (optionalProduct.isPresent()) {
                parent = optionalProduct.get();
            }
        }

        deleteChild(product);
        if (parent != null) {
            parent.setChildren(isChild(parent));
            repository.save(parent);
        }
    }

    public void deleteChild(Product product) {
        List<Product> ch = repository.findAllByParentid(product.getId());
        for (Product p : ch) {
            deleteChild(p);
            repository.delete(p);
            repository.flush();
        }
        repository.delete(product);
        repository.flush();
    }

    private boolean isChild(Product product) {
        List<Product> children = repository.findAllByParentid(product.getId());
        return children.size() != 0;
    }



  /*  public ProductSite add2(ProductSite productSite) {
        // Есть ли запись в базе
        //Optional<Product> optionalProduct = repository.findById(productSite.getId());
        //if (!optionalProduct.isPresent()) {
        Product productDb = new Product();
        productDb.setText(productSite.getText());
        //productDb.setParentId(productSite.getParentId());
        productDb.setChildren(false);
        repository.save(productDb);
        //productSite.setId(productDb.getId());
        //productSite.setChildren(productDb.isChildren());

        //tParentChild(productSite, productDb);
        // }
        return productSite;
    }*.




 /*   public void createUsers(Users users) {
        usersRepository.save(users);
    }



    public Users findById(Long userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    public List<Users> findAllByName(String name) {
        return usersRepository.findAllByName(name);
    }

    public List<Users> findWhereEmailIsGmail() {
        return usersRepository.findWhereEmailIsGmail();
    }

    public List<Users> findWhereNameStartsFromSmith() {
        return usersRepository.findWhereNameStartsFromSmith();
    }*/
}