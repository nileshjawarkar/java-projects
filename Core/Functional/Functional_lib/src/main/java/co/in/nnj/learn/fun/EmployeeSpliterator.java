package co.in.nnj.learn.fun;

import java.util.Spliterator;
import java.util.function.Consumer;

import co.in.nnj.learn.fun.Employee.EmployeeBuilder;

public class EmployeeSpliterator implements Spliterator<Employee> {

    private final Spliterator<String> wordSplitrator;

    public EmployeeSpliterator(final Spliterator<String> wordSplitrator) {
        this.wordSplitrator = wordSplitrator;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Employee> action) {
        final EmployeeBuilder builder = Employee.builder();
        if (wordSplitrator.tryAdvance(id -> builder.withId(Long.valueOf(id))) &&
                wordSplitrator.tryAdvance(name -> builder.withName(name)) &&
                wordSplitrator.tryAdvance(gen -> builder.withGender(gen)) &&
                wordSplitrator.tryAdvance(city -> builder.withCity(city)) &&
                wordSplitrator.tryAdvance(des -> builder.withDesignation(des)) &&
                wordSplitrator.tryAdvance(sal -> builder.withSalary(Double.valueOf(sal).doubleValue()))) {
            action.accept(builder.build());
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Employee> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return wordSplitrator.estimateSize() / 6;
    }

    @Override
    public int characteristics() {
        return wordSplitrator.characteristics();
    }

}
