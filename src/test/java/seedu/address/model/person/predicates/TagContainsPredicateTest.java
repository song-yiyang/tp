package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.TagFilter;
import seedu.address.testutil.PersonBuilder;

public class TagContainsPredicateTest {

    @Test
    public void equals() {
        List<TagFilter> firstPredicateTags = Collections.singletonList(new TagFilter("department:engineering"));
        List<TagFilter> secondPredicateTags =
                Arrays.asList(new TagFilter("department:engineering"), new TagFilter("level:senior"));

        TagContainsPredicate firstPredicate = new TagContainsPredicate(firstPredicateTags);
        TagContainsPredicate secondPredicate = new TagContainsPredicate(secondPredicateTags);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsPredicate firstPredicateCopy = new TagContainsPredicate(firstPredicateTags);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different tags -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emptyFilters_matchesAnyPerson() {
        TagContainsPredicate predicate = new TagContainsPredicate(Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void testSingleFilterNameAndValue_matchesCaseInsensitively() {
        TagContainsPredicate predicate = new TagContainsPredicate(
                Collections.singletonList(new TagFilter("department:engineering")));
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));

        predicate = new TagContainsPredicate(
                Collections.singletonList(new TagFilter("Department:engineering")));
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));

        predicate = new TagContainsPredicate(
                Collections.singletonList(new TagFilter("department:ENGINEERING")));
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
    }

    @Test
    public void testSingleFilterNameAndValue_usesSubstringMatching() {
        TagContainsPredicate predicate = new TagContainsPredicate(
                Collections.singletonList(new TagFilter("part:engine")));
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));

        predicate = new TagContainsPredicate(Collections.singletonList(new TagFilter("part")));
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
    }

    @Test
    public void test_sameTagNameFilters_areOrCombined() {
        // same tag-name group: department=sales OR department=engineering
        TagContainsPredicate predicate = new TagContainsPredicate(Arrays.asList(
                new TagFilter("department:sales"),
                new TagFilter("department:engineering")));
        assertTrue(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
    }

    @Test
    public void test_differentTagNameGroups_areAndCombined() {
        // (department=sales OR department=engineering) AND level=senior
        TagContainsPredicate predicate = new TagContainsPredicate(Arrays.asList(
                new TagFilter("department:sales"),
                new TagFilter("department:engineering"),
                new TagFilter("level:senior")));
        assertTrue(predicate.test(new PersonBuilder()
                .withTags("department:engineering", "level:senior").build()));
        assertTrue(predicate.test(new PersonBuilder()
                .withTags("department:engineering", "level:senior", "project:webapp").build()));
    }

    @Test
    public void testSingleFilterNonMatchingName_returnsFalse() {
        TagContainsPredicate predicate = new TagContainsPredicate(
                Collections.singletonList(new TagFilter("role:manager")));
        assertFalse(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
    }

    @Test
    public void testSingleFilterNonMatchingValue_returnsFalse() {
        TagContainsPredicate predicate = new TagContainsPredicate(
                Collections.singletonList(new TagFilter("department:sales")));
        assertFalse(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
    }

    @Test
    public void testDifferentTagNameGroupsMissingOneGroup_returnsFalse() {
        TagContainsPredicate predicate = new TagContainsPredicate(Arrays.asList(
                new TagFilter("department:engineering"),
                new TagFilter("level:senior")));
        assertFalse(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void testSameTagNameFiltersNoneMatch_returnsFalse() {
        TagContainsPredicate predicate = new TagContainsPredicate(Arrays.asList(
                new TagFilter("department:sales"),
                new TagFilter("department:finance")));
        assertFalse(predicate.test(new PersonBuilder().withTags("department:engineering").build()));
    }

    @Test
    public void testOneAndGroupFailsEvenWhenAnotherGroupMatches_returnsFalse() {
        TagContainsPredicate predicate = new TagContainsPredicate(Arrays.asList(
                new TagFilter("department:sales"),
                new TagFilter("department:engineering"),
                new TagFilter("level:junior")));
        assertFalse(predicate.test(new PersonBuilder()
                .withTags("department:engineering", "level:senior").build()));
    }

    @Test
    public void toStringMethod() {
        List<TagFilter> tags = Arrays.asList(
                new TagFilter("department:engineering"),
                new TagFilter("level:senior"));
        TagContainsPredicate predicate = new TagContainsPredicate(tags);

        String expected = TagContainsPredicate.class.getCanonicalName() + "{tagFilters=" + tags + "}";
        assertEquals(expected, predicate.toString());
    }
}
