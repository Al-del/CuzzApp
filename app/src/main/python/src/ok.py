import yaml
import pkg_resources

# Get installed distributions
dists = [d for d in pkg_resources.working_set]

# Prepare data for YAML
data = {dist.key: dist.version for dist in dists}

# Write data to a YAML file
with open('libraries.yaml', 'w') as file:
    yaml.dump(data, file)