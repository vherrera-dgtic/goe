package es.caib.goe.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Representació d'un procediment. A nivell de classe definim la seqüència que emprarem, i les claus úniques.
 *
 * @author areus
 */
@Entity
@SequenceGenerator(name = "procediment-sequence", sequenceName = "GOE_PROCEDIMENT_SEQ", allocationSize = 1)
@Table(name = "GOE_PROCEDIMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "GOE_PROCEDIMENT_CODISIA_UK", columnNames = "CODISIA")
        },
        indexes = {
                @Index(name = "GOE_PROCEDIMENT_PK_I", columnList = "ID"),
                @Index(name = "GOE_PROCEDIMENT_CODISIA_UK_I", columnList = "CODISIA"),
                @Index(name = "GOE_PROCEDIMENT_UNITAT_FK_I", columnList = "UNITATORGANICAID")
        }
)
@NamedQueries({
        @NamedQuery(name = Procediment.FIND_BY_CODISIA,
                query = "select p from Procediment p where p.codiSia = :codiSia"),
        @NamedQuery(name = Procediment.COUNT_BY_IDUNITAT,
                query = "select count(p) from Procediment p join p.unitatOrganica u where u.id = :idUnitat"),
        @NamedQuery(name = Procediment.FIND_DTO_BY_IDUNITAT,
                query = "select new es.caib.goe.service.model.ProcedimentDTO(p.id, p.codiSia, p.nom, u.id) " +
                        "from Procediment p join p.unitatOrganica u where u.id = :idUnitat order by p.id")
})
public class Procediment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_CODISIA = "Procediment.FIND_BY_CODISIA";
    public static final String COUNT_BY_IDUNITAT = "Procediment.COUNT_BY_IDUNITAT";
    public static final String FIND_DTO_BY_IDUNITAT = "Procediment.FIND_DTO_BY_IDUNITAT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procediment-sequence")
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     * Codi SIR que identifica el procediment. És únic, i per tant una clau natural.
     * Ha de ser un nombre de entre 6 i 8 dígits.
     * No es pot actualitzar una vegada creat.
     */
    @Column(name = "CODISIA", nullable = false, updatable = false, length = 8)
    @NotNull @Pattern(regexp = "[0-9]{6,8}", message = "{codiSia.Pattern.message}")
    private String codiSia;

    /**
     * Nom del procediment.
     */
    @Column(name = "NOM", nullable = false, length = 50)
    @NotEmpty @Size(max = 50)
    private String nom;

    /**
     * La unitat orgànica responsable del procediment. Marcam com a transient per JSON per evitar que es
     * seralitzi/deserialitzi.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNITATORGANICAID", nullable = false,
            foreignKey = @ForeignKey(name = "GOE_PROCEDIMENT_UNITAT_FK"))
    private UnitatOrganica unitatOrganica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiSia() {
        return codiSia;
    }

    public void setCodiSia(String codiSia) {
        this.codiSia = codiSia;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public UnitatOrganica getUnitatOrganica() {
        return unitatOrganica;
    }

    public void setUnitatOrganica(UnitatOrganica unitatOrganica) {
        this.unitatOrganica = unitatOrganica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Procediment)) return false;
        Procediment that = (Procediment) o;
        return Objects.equals(codiSia, that.codiSia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiSia);
    }

    @Override
    public String toString() {
        return "Procediment{" +
                "id=" + id +
                ", codiSia='" + codiSia + '\'' +
                '}';
    }
}