require 'open3'

task :default do 
  puts "Testing hugo build\n\n"
  stdout, stderr, status = Open3.capture3('cd hugo && hugo --verbose')
  puts "stderr:\n #{stderr}"
  puts "stdout:\n #{stdout}"

end
