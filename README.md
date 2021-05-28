# leo-lang

Leonardo programming language.

##### Getting

```
git clone https://github.com/micapolos/leo-lang.git
```

##### Updating

```
git pull
```

##### Building with Gradle

```
cd leo-lang
gradle jar
```

##### Installing

Add `leo-lang\bin` to your `$PATH`.

##### Running

`cd` to the root directory containing all Leonardo libraries.

```
cd leo
```

Edit `sandbox.leo` and run:

```
leo sandbox
```

Or... create new `.leo` file in any sub-directory (in example `micapolos\examples\main.leo`), and run it from there:

```
leo micapolos example main
```

##### Testing

Runs all Leonardo tests:

```
leo test all
```
