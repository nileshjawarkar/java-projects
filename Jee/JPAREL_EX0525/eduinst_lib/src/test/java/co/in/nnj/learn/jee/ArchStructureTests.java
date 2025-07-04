package co.in.nnj.learn.jee;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

public class ArchStructureTests {
    private static JavaClasses all_classes;

    @BeforeAll
    public static void init() {
        //-- final ImportOption ignoreTests = new ImportOption() {
        //--     @Override
        //--     public boolean includes(final Location location) {
        //--         System.out.println(" =>" + location);
        //--         return !(location.contains("/test-classes/") || location.contains("/.m2/"));
        //--     }
        //-- };

        //-- all_classes = new ClassFileImporter().withImportOption(ignoreTests).importPackages("co.in.nnj.learn.jee");
        all_classes = new ClassFileImporter().importPackages("co.in.nnj.learn.jee");
    }

    @Test
    public void pkgsShouldNotAccesseAdapter() {
        final ArchRule ruleForDomain = noClasses()
                .that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..adapter..");
        ruleForDomain.check(all_classes);

        final ArchRule ruleForPort = noClasses()
                .that().resideInAPackage("..port..")
                .should().accessClassesThat().resideInAPackage("..adapter..");
        ruleForPort.check(all_classes);
    }

    @Test
    public void pkgsShouldNotAccessEachOther() {
        final JavaClasses adapter_classes = new ClassFileImporter().importPackages("co.in.nnj.learn.jee.adapter");
        final ArchRule ruleForInput = noClasses()
                .that().resideInAPackage("..input..")
                .should().accessClassesThat().resideInAPackage("..output..");
        ruleForInput.check(adapter_classes);
        final ArchRule ruleForOutput = noClasses()
                .that().resideInAPackage("..output..")
                .should().accessClassesThat().resideInAPackage("..input..");
        ruleForOutput.check(adapter_classes);
    }

    @Test
    public void pkgsShouldNotAccessPort() {
        /*
         * final ArchRule ruleForEntity = noClasses()
         * .that().resideInAPackage("..domain.entity..")
         * .should().accessClassesThat().resideInAPackage("..port..");
         * ruleForEntity.check(all_classes);
         */

        final ArchRule ruleForValueObjs = noClasses()
                .that().resideInAPackage("..domain.valueobjects..")
                .should().accessClassesThat().resideInAPackage("..port..");
        ruleForValueObjs.check(all_classes);
    }

    @Test
    public void pkgsShouldNotAccessServices() {
        final JavaClasses domain_classes = new ClassFileImporter().importPackages("co.in.nnj.learn.jee.domain");
        /*
         * final ArchRule ruleForEntity = noClasses()
         * .that().resideInAPackage("..entity..")
         * .should().accessClassesThat().resideInAPackage("..service..");
         * ruleForEntity.check(domain_classes);
         */

        final ArchRule ruleForValueObjs = noClasses()
                .that().resideInAPackage("..valueobjects..")
                .should().accessClassesThat().resideInAPackage("..service..");
        ruleForValueObjs.check(domain_classes);
    }
}
