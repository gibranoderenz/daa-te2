import random

# Source: https://stackoverflow.com/a/70255988
# (dengan modifikasi)
def get_target_sum_list(dataset_length, target_sum):
    amounts = [random.random() for _ in range(dataset_length)]
    asum = sum(amounts)
    for i in range(dataset_length):
        amounts[i] = 1 + int(amounts[i]*(target_sum - dataset_length) / asum)
    asum = sum(amounts)
    for i in range(target_sum - asum):
        amounts[random.randint(0,4)] += 1
    
    return amounts

def generate_dataset(dataset_length, target_sum):
    left_subset = get_target_sum_list(dataset_length, target_sum)
    right_subset = get_target_sum_list(dataset_length, target_sum)
    left_subset.extend(right_subset)
    random.shuffle(left_subset)

    with open(f'{dataset_length * 2}.txt', 'w') as file:
        for num in left_subset:
            file.write(str(num) + '\n')

def load_dataset(dataset_length):
    dataset = []
    with open(f'{dataset_length}.txt', 'r') as file:
        for line in file:
            dataset.append(int(line))
    return dataset

# for generating the dataset
if __name__ == '__main__':
    configs = [(5, 32), (20, 200), (40, 500)] # format: (subset length, subset sum)
    for config in configs:
        length, target = config
        generate_dataset(length, target)