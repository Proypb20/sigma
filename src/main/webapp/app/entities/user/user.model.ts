export interface IUser {
  id: number;
  login?: string;
  lastName?: string;
  firstName?: string;
}

export class User implements IUser {
  constructor(public id: number, public login: string, public lastName: string, public firstName: string) {}
}

export function getUserIdentifier(user: IUser): number {
  return user.id;
}
