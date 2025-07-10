package Tienda.Web.dao;

import Tienda.Web.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoDao extends JpaRepository <Producto, Long> {
    public List<Producto> findByPrecioBetweenOrderByDescripcion(double precioInf, double precioSup);
}
 