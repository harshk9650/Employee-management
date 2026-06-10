package com.ems.repository;

import com.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EmployeeRepository - Spring Data JPA Repository interface.
 *
 * By extending JpaRepository<Employee, Long>:
 *   - Spring auto-generates all standard CRUD implementations at runtime
 *   - No need to write SQL or boilerplate DAO code
 *
 * Built-in methods inherited from JpaRepository:
 *   save(entity)          -> INSERT or UPDATE
 *   findById(id)          -> SELECT WHERE id = ?
 *   findAll()             -> SELECT * FROM employee
 *   deleteById(id)        -> DELETE WHERE id = ?
 *   existsById(id)        -> checks if record exists
 *   count()               -> SELECT COUNT(*)
 *
 * Custom query methods (Spring Data derives SQL from method name):
 *   findByEmail()         -> SELECT * WHERE email = ?
 *   existsByEmail()       -> SELECT COUNT(*) WHERE email = ?
 *   findByDepartment()    -> SELECT * WHERE department = ?
 *
 * @JPQL queries:
 *   Uses @Query for more complex queries in JPQL (Java Persistence Query Language).
 *   JPQL references entity class names and field names, not table/column names.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find an employee by their exact email address.
     * Used during create/update to enforce email uniqueness.
     *
     * Spring Data derives: SELECT * FROM employee WHERE email = ?1
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Check if an employee with the given email already exists.
     * More efficient than findByEmail() when you only need a boolean.
     *
     * Spring Data derives: SELECT COUNT(*) > 0 FROM employee WHERE email = ?1
     */
    boolean existsByEmail(String email);

    /**
     * Check if another employee (different ID) has the same email.
     * Used during UPDATE to allow keeping the same email, while
     * blocking it if a DIFFERENT employee already owns that email.
     *
     * Spring Data derives: SELECT COUNT(*) > 0 WHERE email = ? AND id != ?
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Find all employees in a given department (case-insensitive).
     *
     * @Query uses JPQL — references Employee entity class and its field names.
     * LOWER() ensures "engineering" matches "Engineering" or "ENGINEERING".
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.department) = LOWER(:department)")
    List<Employee> findByDepartmentIgnoreCase(@Param("department") String department);

    /**
     * Search employees by name (partial, case-insensitive).
     * Useful for a search bar feature.
     *
     * LIKE %:name% performs a "contains" search.
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> searchByName(@Param("name") String name);
}
