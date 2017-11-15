---
title: "Getting Unstuck - Reference Guide"
date: 2017-11-02
tags: [getting-unstuck, reference-guide, talks	]
draft: false
showDate: false
---

Are you excited to try using the scientific method for debugging? This is a short reference sheet to refer back to the next time you get stuck in a debugging session, to make the most of the scientific method framework.

This guide comes from my talk "Getting Unstuck: Using the Scientific Method for Debugging". Slides can be found [here](https://bit.ly/getting-unstuck-slides). Video of the talk is coming soon.

## Gather existing knowledge
Write down what you know. Brain dump everything you know about your bug. From how the user-facing impact to any interesting log lines you've seen, and what you did to try to understand the bug yesterday, write it all down.

## Ask questions. Pick one.
What questions do you have about your bug? What are you seeing that doesn't make sense? What do you think might be happening but you're not sure? 

What "existing knowledge" did you write down which is actually an assumption? Often the most useful question for your experiment is "Is this assumption correct?"

Pick one question. Make it detailed. It's helpful to think about "when", "where", and "if".

## Make a hypothesis (educated guess)
What do you think is the answer to your question? What is a possible answer?

Write down a very specific statement about what you think is happening in your program in relation to your question.

You can frame it as a [null hypothesis](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&cad=rja&uact=8&ved=0ahUKEwiImojsl7_XAhVJy2MKHdzWAgkQFgg2MAE&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FNull_hypothesis&usg=AOvVaw2bJpSr_37x4hZWN5q0zmGP), but you don't have to. Again, "when", "if", and "where" clauses are your friend.

## Design an experimental procedure
What information would you need to gather to disprove your hypothesis? Or alternatively, to make you feel confident your hypothesis is true? What steps do you need to take to gather that information?

Be detailed, and include what you expect to observe.

## Run the experiment and take good notes
Write down what you observe that you expected to see. And what you didn't expect to see. And the surprising things you never imagined seeing your system do. 

If you encounter separate bugs, set them aside in your bug tracker for another day. Don't get distracted from your experiment.

## Make a conclusion
Do you understand your bug and how to fix it now? Did you rule out your hypothesis as the cause of the bug? Do you have new questions?

## Return to the gathering existing knowledge step
You've learned new things about your bug. At an absolute minimum, you've ruled out one of 6000+ causes of your bug. That's huge!

If you've ruled out one out of 6000 causes but still don't know what's going on, see if new questions arose from your experiement or if one of your old questions is still interesting.

## Share what you learned
- Issues that may come up again that your teammates can be prepared for
- Other bugs you found
- Step by step instructions for how you approach debugging your system for a new person onboarding to your team
- Other information your teammates or customers will find helpful

Tell me if you find the scientific method useful for your debugging! You can reach me on Twitter at [@CarolineTaymor](https://twitter.com/carolinetaymor) or elsewhere via the contact links on this site.