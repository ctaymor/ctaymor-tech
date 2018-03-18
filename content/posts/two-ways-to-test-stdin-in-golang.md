---
title: "Two Ways to Test Stdin in Golang"
date: 2018-03-05
tags: [golang, testing, ginkgo,]
draft: true
showDate: true
---

I recently had the opportunity to test how I was handling information from stdin in two different Golang programs. It's not obvious how to test against stdin, and I ended up using a different approach on each project.

I'll show you the two methods I found of testing against stdin, and then I'll tell you which I prefer and why.

## What is Stdin?

Stdin is short for Standard Input. Stdin is the connection between a program and the environment it was started in. For a program running at the command line, this is usually text input typed into the terminal, but it might be piped in from another process as well.

In this case, I was building command line interfaces with interactive prompts, so stdin was coming from text typed into the terminal.

# Method 1

The first is to inject a `bufio.Scanner` into the function.
In tests, I pass something like this:
```
reader, writer := io.Pipe()
inputScanner := bufio.NewScanner(reader)
myFunc(inputScanner)
```
and in the real code, I pass `os.Stdin` as the argument to myFunc.

In the tests, I can then write whatever the user "types" to the `writer`.

```
writer.Write([]byte("yes, delete the thing\n"))
```

# Method 2
The second approach I've tried is to overwrite stdin in tests, like so:
```
reader, writer, err := os.Pipe()
Expect(err).NotTo(HaveOccurred())

os.Stdin = reader
```

In this case, I send test input in the exact same manner as method 1.

```
writer.Write([]byte("yes, delete the thing\n"))
```

# Which do I prefer
The benefit to the first approach is it makes explict the interface with Stdin. It also does not rely on side effects. There's something alarming to me about overwriting Stdin.

However, ultimately, I prefer the second approach. It does feel like an intense side effect, but I think injecting Stdin just for testing purposes is too large a change to make to my code just for tests.

If you have another way of approaching this, or prefer one of the two methods, I'd love to hear.