package co.in.nnj.learn.jee;

import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

public class ArchChecker {

    @Test
    public void pkgAdapterShouldNotBeAccessedBy() {
        final JavaClasses all_classes = new ClassFileImporter().importPackages("co.in.nnj.learn.jee");
        final ArchRule ruleForAdapter1 = noClasses()
                .that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..adapter..");
        ruleForAdapter1.check(all_classes);

        final ArchRule ruleForAdapter2 = noClasses()
                .that().resideInAPackage("..port..")
                .should().accessClassesThat().resideInAPackage("..adapter..");
        ruleForAdapter2.check(all_classes);
    }

    @Test
    public void pkgInputOutputShouldNotAccessEachOther() {
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
}
