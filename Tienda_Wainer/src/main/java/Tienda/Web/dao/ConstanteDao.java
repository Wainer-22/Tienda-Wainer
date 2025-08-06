package Tienda.Web.dao;

import Tienda.Web.domain.Constante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstanteDao 
        extends JpaRepository<Constante,Long> {
    
    public Constante findByAtributo(String stributo);
}
