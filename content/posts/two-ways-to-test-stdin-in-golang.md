---
title: "Two Ways to Test Stdin in Golang"
date: 2018-03-05
tags: [golang, testing, ginkgo,]
draft: true
showDate: true
---

# Intro
## What will I show you

I recently had the opportunity to test how I was handling information from stdin in two different Golang programs, using the (Ginkgo)[https://onsi.github.io/ginkgo/] testing framework with the (Gomega matcher library)[https://onsi.github.io/gomega/]. It's not obvious how to test against stdin, and I ended up using a different approach on each project.

I'll show you the two methods I found of testing against stdin, and then I'll share my thoughts on the relative merits of these two approaches.

## Caroline, what is Stdin?

Stdin is short for Standard In. Stdin is the method of inputing information into a program. Boy that's a crap answer. Do more research to understand deeper so I can explain better
## Setup
Let's take as an example, a program to write which listens for user input and writes out "success" if the user types "iguana" and "no lizards" if the user types anything else. We'll write tests for it first, for each method, and show how the tests shape the code we ultimately write.

Note that I will assume you have your test suite set up correctly, and will show only the relevant files. However, if you want to follow along at home, the source code for both examples can be found here:
# Method 1
# Method 2
# Conclusion
## What I showed you
## Which I prefer
## Which do you prefer