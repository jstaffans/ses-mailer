# ses-mailer

A thin, partial Clojure wrapper around the Amazon SES Java API. I wrote this because I wanted to 
rely on instance profiles for AWS access control instead of using [Postal](https://github.com/drewr/postal) and having to 
configure SMTP credentials client-side.

## Usage

```clj
(send-email client-opts
  "no-reply@company.com"
  "lucky-customer@me.com"
  "You are our millionth customer!"
  {:html_body "<html>Awesome html content</html>
   :text_body "Boring text content"}
```

Where client-opts may be:

```clj
# Using the default provider chain falls back to the instance profile
{:provider (DefaultAWSCredentialsProviderChain.)}
# Or use keys directly
{:access-key "..." :secret-key "..."}

```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
