require 'open3'

task :default do 
  puts "Testing hugo build\n\n"
  stdout, stderr, status = Open3.capture3('cd hugo && hugo --verbose')
  raise "`hugo` failed:\n #{stderr}" unless stderr.empty?
 
  puts "stdout:\n #{stdout}"

end
