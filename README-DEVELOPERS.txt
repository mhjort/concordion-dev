Development Rules
-----------------

* Don't break existing behaviour. Backward compatibility is 
  extremely important.

* New feature proposals should be described with acceptance tests 
  and ideally discussed with the group before implementation.

* Follow the style and conventions of the existing code (basically
  Sun's conventions). In particular:
  - Use 4 spaces (not tabs) 
  - Always use braces after "if" statements.

* All code changes should have automated tests of some sort.

* Never check a failing test into the repository. (Though you can
  check-in unimplemented acceptance test HTML for new feature ideas). 



Building
--------

mvn clean install