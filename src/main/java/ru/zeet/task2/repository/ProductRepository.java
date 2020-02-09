package ru.zeet.task2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.zeet.task2.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByParentid(Integer Parentid);

    //просто правильное название метода даст возможность избежать запросов на SQL
    List<Product> findAllByText(String text);

    @Query("select u from Product u where u.text like '%@gmail.com%'")
    //если этого мало можно написать
        //собственный запрос на языке похожем на SQL
    List<Product> findWhereEmailIsGmail();

    @Query(value = "select * from Product where name like '%smith%'", nativeQuery = true)
        //если и этого мало - можно написать запрос на чистом SQL и все это будет работать
    List<Product> findWhereNameStartsFromSmith();

    @Query("select u from Product u  where u.parentid is null") // where u.parentId = '0'
    List<Product> findRoot();
}